package me.aviaday.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import me.aviaday.utils.Logger
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<S : ViewState, A : Action>(
    private val dependency: BaseViewModelDependency,
    initialState: S
) : ViewModel(), CoroutineScope {

    private val unexpectedErrorHandler = CoroutineExceptionHandler { _, throwable ->
        logger.e("Unexpected error", throwable)
        dependency.errorHandler.handle(throwable)
    }

    private val logger: Logger = dependency.logger

    private val loadingState = ObservableLoadingCounter()
    private val atomicState = AtomicReference(initialState)
    protected val state: S
        get() = atomicState.get()

    val viewState = MutableLiveData<S>()
    val actions = SingleLiveEvent<A>()

    override val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + unexpectedErrorHandler

    init {
        launch {
            loadingState.observable
                .collect { handleLoading(it) }
        }
        viewState.value = initialState
    }

    protected open fun handleLoading(isLoading: Boolean) = Unit

    protected open fun handleGeneralError(e: Throwable) {
        logger.e("Execution error", e)
        dependency.errorHandler.handle(e)
    }

    protected fun setState(reducer: S.() -> S) {
        while (true) {
            val prevState = atomicState.get()
            val newState = prevState.reducer()
            if (newState != prevState) {
                if (atomicState.compareAndSet(prevState, newState)) {
                    viewState.postValue(newState)
                    return
                }
            } else {
                return
            }
        }
    }

    protected fun sendAction(action: A) {
        launch(Dispatchers.Main) {
            actions.value = action
        }
    }

    protected fun CoroutineScope.launchSafe(
        loading: (Boolean) -> Unit = { if (it) loadingState.addLoader() else loadingState.removeLoader() },
        error: (Throwable) -> Unit = { handleGeneralError(it) },
        body: suspend () -> Unit
    ): Job =
        launch {
            try {
                loading(true)
                withContext(Dispatchers.IO) {
                    body.invoke()
                }
                loading(false)
            } catch (e: Throwable) {
                loading(false)
                error(e)
            }
        }

    protected fun <T> Flow<T>.launchFlowSafe(
        loading: (Boolean) -> Unit = { if (it) loadingState.addLoader() else loadingState.removeLoader() },
        error: (Throwable) -> Unit = { handleGeneralError(it) },
        onEach: (item: T) -> Unit
    ): Job {
        return flowOn(Dispatchers.IO)
            .onStart {
                loading(true)
            }
            .withIndex()
            .onEach {
                if (it.index == 0) {
                    loading(false)
                }
                onEach(it.value)
            }
            .catch {
                loading(false)
                error(it)
            }
            .launchIn(viewModelScope)
    }
}

interface ViewState
interface Action

object EmptyState : ViewState

data class BaseViewModelDependency(
    val context: Context,
    val logger: Logger,
    val errorHandler: ErrorHandler
)
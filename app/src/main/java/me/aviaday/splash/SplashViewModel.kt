package me.aviaday.splash

import me.aviaday.base.Action
import me.aviaday.base.BaseViewModel
import me.aviaday.base.BaseViewModelDependency
import me.aviaday.base.EmptyState
import me.aviaday.usecases.CheckOnboardingShownUseCase

class SplashViewModel(
    baseViewModelDependency: BaseViewModelDependency,
    private val checkOnboardingShownUseCase: CheckOnboardingShownUseCase
) : BaseViewModel<EmptyState, SplashAction>(baseViewModelDependency, EmptyState) {

    init {
        checkOnboardingShown()
    }

    private fun checkOnboardingShown() {
        val isOnboardingShown = checkOnboardingShownUseCase.invoke()
        if (isOnboardingShown) {
            sendAction(SplashAction.OpenEntryScreen)
        } else {
            sendAction(SplashAction.OpenOnboardingScreen)
        }
    }
}

sealed class SplashAction : Action {
    object OpenOnboardingScreen : SplashAction()
    object OpenEntryScreen : SplashAction()
}
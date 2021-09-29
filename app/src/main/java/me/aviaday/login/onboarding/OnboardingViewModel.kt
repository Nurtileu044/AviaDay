package me.aviaday.login.onboarding

import me.aviaday.base.Action
import me.aviaday.base.BaseViewModel
import me.aviaday.base.BaseViewModelDependency
import me.aviaday.base.EmptyState
import me.aviaday.usecases.SaveOnboardingShownUseCase

class OnboardingViewModel(
    viewModelDependency: BaseViewModelDependency,
    private val saveOnboardingShownUseCase: SaveOnboardingShownUseCase
) : BaseViewModel<EmptyState, OnboardingAction>(viewModelDependency, EmptyState) {

    init {

    }

    fun onSkipClicked() {
        saveOnboardingShown()
    }

    fun onStartClicked() {
        saveOnboardingShown()
    }

    private fun saveOnboardingShown() {
        saveOnboardingShownUseCase.invoke()
        sendAction(OnboardingAction.OpenEntryScreen)
    }
}

sealed class OnboardingAction : Action {
    object OpenEntryScreen : OnboardingAction()
}
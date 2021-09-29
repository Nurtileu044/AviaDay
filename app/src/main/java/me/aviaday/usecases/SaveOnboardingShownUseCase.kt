package me.aviaday.usecases

import me.aviaday.preferences.Preferences
import me.aviaday.preferences.PreferencesImpl.Companion.PREF_KEY_ONBOARDING_SHOWN

class SaveOnboardingShownUseCase(
    private val preferences: Preferences
) {

    fun invoke() {
        preferences.putBoolean(PREF_KEY_ONBOARDING_SHOWN, true)
    }
}
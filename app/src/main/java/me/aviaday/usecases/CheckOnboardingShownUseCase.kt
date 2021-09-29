package me.aviaday.usecases

import me.aviaday.preferences.Preferences
import me.aviaday.preferences.PreferencesImpl.Companion.PREF_KEY_ONBOARDING_SHOWN

class CheckOnboardingShownUseCase(
    private val preferences: Preferences
) {

    fun invoke(): Boolean {
        return preferences.getBoolean(PREF_KEY_ONBOARDING_SHOWN, false)
    }
}
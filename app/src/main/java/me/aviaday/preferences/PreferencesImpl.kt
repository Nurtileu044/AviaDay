package me.aviaday.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesImpl(
    private val gson: Gson,
    private val preferences: SharedPreferences
) : Preferences {

    companion object {
        const val PREF_KEY_ONBOARDING_SHOWN = "PREF_KEY_ONBOARDING_SHOWN"
        const val PREF_KEY_NEED_BIOMETRIC = "PREF_KEY_NEED_BIOMETRIC"
        const val PREF_KEY_HAS_IIN = "PREF_KEY_HAS_IIN"
        const val PREF_KEY_PIN_CODE = "PREF_KEY_PIN_CODE"
        const val PREF_KEY_ADD_APARTMENT_FINISHED = "PREF_KEY_ADD_APARTMENT_FINISHED"
        const val PREF_KEY_TASTAMAT = "PREF_KEY_TASTAMAT"
        const val PREF_KEY_PAYMENT_SUCCESS = "PREF_KEY_PAYMENT_SUCCESS"
        const val PREF_KEY_PAYMENT_FAILED = "PREF_KEY_PAYMENT_FAILED"
        const val PREF_KEY_ORDER_ID = "PREF_KEY_ORDER_ID"
        const val PREF_KEY_HOUSE_CARD_SUCCESS = "PREF_KEY_HOUSE_CARD_SUCCESS"
        const val PREF_KEY_HOUSE_CARD_FAILED = "PREF_KEY_HOUSE_CARD_FAILED"
        const val PREF_KEY_TOKEN_EXIST = "PREF_KEY_TOKEN_EXIST"
        const val PREF_KEY_FILTER_ADDRESS = "PREF_KEY_FILTER_ADDRESS"
        const val PREF_KEY_FILTER_STATUS = "PREF_KEY_FILTER_STATUS"
        const val PREF_KEY_FILTER_WORKTYPE = "PREF_KEY_FILTER_WORKTYPE"
        const val PREF_KEY_FILTER_DATE = "PREF_KEY_FILTER_DATE"
        const val PREF_KEY_FILTER_LATENESS = "PREF_KEY_FILTER_LATENESS"
    }

    override fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    override fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)

    override fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    override fun getLong(key: String, defaultValue: Long): Long =
        preferences.getLong(key, defaultValue)

    override fun putString(key: String, value: String?) {
        preferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String, defaultValue: String?): String? =
        preferences.getString(key, defaultValue)

    override fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        preferences.getBoolean(key, defaultValue)

    override fun <T> putObject(key: String, value: T?) {
        if (value == null) {
            remove(key)
        } else {
            val jObject = gson.toJson(value)
            putString(key, jObject)
        }
    }

    override fun <T : Any> getObject(key: String, classOf: TypeToken<T>): T? {
        val jObject = getString(key, null)
        return if (jObject == null) {
            null
        } else {
            gson.fromJson(jObject, classOf.type)
        }
    }

    override fun contains(key: String): Boolean = preferences.contains(key)

    override fun clear() {
        preferences.edit().clear().apply()
    }
}
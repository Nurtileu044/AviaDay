package me.aviaday.preferences

import com.google.gson.reflect.TypeToken

interface Preferences {
    fun remove(key: String)

    fun putInt(key: String, value: Int)

    fun getInt(key: String, defaultValue: Int): Int

    fun putLong(key: String, value: Long)

    fun getLong(key: String, defaultValue: Long): Long

    fun putString(key: String, value: String?)

    fun getString(key: String, defaultValue: String? = null): String?

    fun putBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun <T> putObject(key: String, value: T?)

    fun <T : Any> getObject(key: String, classOf: TypeToken<T>): T?

    fun contains(key: String): Boolean

    fun clear()
}

inline fun <reified T : Any> Preferences.getObject(key: String): T? =
    getObject(key, object : TypeToken<T>() {})
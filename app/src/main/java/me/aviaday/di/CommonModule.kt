package me.aviaday.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import me.aviaday.base.BaseViewModelDependency
import me.aviaday.base.ErrorHandler
import me.aviaday.preferences.Preferences
import me.aviaday.preferences.PreferencesImpl
import me.aviaday.utils.DefaultLogger
import me.aviaday.utils.Logger
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val PREFERENCE_NAME = "prefs"

object CommonModule : InjectionModule {

    private const val DEFAULT_CONNECT_TIMEOUT_SECONDS = 30L
    private const val DEFAULT_READ_TIMEOUT_SECONDS = 30L
    private const val DEFAULT_DISK_CACHE_SIZE = 256 * 1024 * 1024L

    const val RETROFIT_EKNOT_BASE = "RETROFIT_EKNOT_BASE"
    const val RETROFIT_EKNOT_WITH_AUTHORIZATION = "RETROFIT_EKNOT_WITH_AUTHORIZATION"
    const val RETROFIT_SMART = "RETROFIT_SMART"

    override fun create(): Module = module {

        single<Logger> { DefaultLogger(BuildConfig.DEBUG) }
        single {
            val errorHandler = ErrorHandler(androidContext())
            BaseViewModelDependency(androidContext(), get(), errorHandler)
        }

        single {
            GsonBuilder()
                .apply { if (BuildConfig.DEBUG) setPrettyPrinting() }
                .create()
        } bind Gson::class

        single {
            createOkHttpClient(get())
                .apply { if (BuildConfig.DEBUG) addLoggingInterceptor(get()) }
                .build()
        } bind OkHttpClient::class

        single {
            val context: Context = get()
            val masterKeyAlias =
                MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

            EncryptedSharedPreferences.create(
                context,
                PREFERENCE_NAME,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } bind SharedPreferences::class

        single { PreferencesImpl(get(), get()) } bind Preferences::class
    }

    private fun createOkHttpClient(context: Context): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .readTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .cache(Cache(context.cacheDir, DEFAULT_DISK_CACHE_SIZE))
            .hostnameVerifier { _, _ -> true }

    private fun OkHttpClient.Builder.addLoggingInterceptor(gson: Gson): OkHttpClient.Builder {
        val okHttpLogTag = "OkHttp"

        val okHttpLogger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (!message.startsWith('{') && !message.startsWith('[')) {
                    Timber.tag(okHttpLogTag).d(message)
                    return
                }

                try {
                    val json = JsonParser.parseString(message)
                    Timber.tag(okHttpLogTag).d(gson.toJson(json))
                } catch (e: Throwable) {
                    Timber.tag(okHttpLogTag).e(message)
                }
            }
        }

        val interceptor = HttpLoggingInterceptor(okHttpLogger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return addInterceptor(interceptor)
    }
}
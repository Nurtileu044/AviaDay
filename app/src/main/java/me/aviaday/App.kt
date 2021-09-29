package me.aviaday

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.aviaday.splash.SplashModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                SplashModule.create()
            )
        }

        /*FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        FirebaseApp.initializeApp(applicationContext)*/
    }
}
package me.aviaday.splash

import me.aviaday.di.InjectionModule
import me.aviaday.usecases.CheckOnboardingShownUseCase
import me.aviaday.usecases.SaveOnboardingShownUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object SplashModule : InjectionModule {

    override fun create(): Module = module {

        single { CheckOnboardingShownUseCase(get()) }
        single { SaveOnboardingShownUseCase(get()) }

        viewModel { SplashViewModel(get(), get()) }
    }
}
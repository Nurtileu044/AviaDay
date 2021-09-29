package me.aviaday.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import me.aviaday.R
import me.aviaday.extensions.replaceFragment
import me.aviaday.login.onboarding.OnboardingFragment
import org.koin.android.viewmodel.ext.android.viewModel

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.actions.observe(viewLifecycleOwner, { action ->
            when (action) {
                is SplashAction.OpenOnboardingScreen -> replaceFragment(
                    OnboardingFragment(),
                    addToBackStack = false
                )
                is SplashAction.OpenEntryScreen -> replaceFragment(
                    OnboardingFragment(),
                    addToBackStack = false
                )
            }
        })
    }
}
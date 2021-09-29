package me.aviaday.login.onboarding

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import me.aviaday.R
import me.aviaday.databinding.FragmentOnboardingBinding
import me.aviaday.extensions.replaceFragment
import me.aviaday.login.onboarding.adapter.OnboardingPagerAdapter
import me.aviaday.login.onboarding.model.OnboardingPage
import org.koin.android.viewmodel.ext.android.viewModel

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val pages: ArrayList<OnboardingPage> = arrayListOf()
    private lateinit var binding: FragmentOnboardingBinding
    private val viewModel: OnboardingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addOnboardingElements()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingBinding.bind(view)

        val adapter = OnboardingPagerAdapter(requireContext(), pages)
        with(binding) {
            pager.adapter = adapter
            tabs.setupWithViewPager(pager, true)
            pager.addOnPageChangeListener(initializeOnPageChangeListener(nextButton))

            skipButton.setOnClickListener { viewModel.onSkipClicked() }
            nextButton.setOnClickListener {
                if (pager.currentItem == pages.size - 1) {
                    viewModel.onStartClicked()
                } else {
                    pager.currentItem += 1
                }
            }
        }

        viewModel.actions.observe(viewLifecycleOwner, { action ->
            when (action) {
                is OnboardingAction.OpenEntryScreen -> openEntryScreen()
            }
        })
    }

    private fun addOnboardingElements() {
        pages.add(
            OnboardingPage(
                R.drawable.image_onboarding_accounting,
                getString(R.string.onboard_accounting_title),
                getString(R.string.onboard_accounting_body),
            )
        )
        pages.add(
            OnboardingPage(
                R.drawable.image_onboarding_ordering,
                getString(R.string.onboard_services_title),
                getString(R.string.onboard_services_body)
            )
        )
        pages.add(
            OnboardingPage(
                R.drawable.image_onboarding_ads,
                getString(R.string.onboard_ads_title),
                getString(R.string.onboard_ads_body)
            )
        )
        pages.add(
            OnboardingPage(
                R.drawable.image_onboarding_news,
                getString(R.string.onboard_news_title),
                getString(R.string.onboard_news_body)
            )
        )
        pages.add(
            OnboardingPage(
                R.drawable.image_onboarding_votes,
                getString(R.string.onboard_votes_title),
                getString(R.string.onboard_votes_body)
            )
        )
    }

    private fun initializeOnPageChangeListener(nextButton: Button) =
        object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                nextButton.text = if (position == pages.size - 1) {
                    getString(R.string.start)
                } else {
                    getString(R.string.next)
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        }

    private fun openEntryScreen() {
        replaceFragment(OnboardingFragment(), addToBackStack = false)
    }
}
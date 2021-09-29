package me.aviaday.login.onboarding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import me.aviaday.databinding.PageOnboardingBinding
import me.aviaday.login.onboarding.model.OnboardingPage

class OnboardingPagerAdapter(
    private val context: Context,
    private val pages: List<OnboardingPage>
) : PagerAdapter() {

    override fun getCount(): Int = pages.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = PageOnboardingBinding.inflate(LayoutInflater.from(context))
        with(binding) {
            Glide.with(context).load(pages[position].image).into(imageView)
            titleTextView.text = pages[position].title
            descriptionTextView.text = pages[position].body
        }
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj
}
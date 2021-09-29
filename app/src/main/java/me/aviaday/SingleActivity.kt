package me.aviaday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import me.aviaday.databinding.ActivitySingleBinding
import me.aviaday.extensions.replaceFragment
import me.aviaday.splash.SplashFragment

class SingleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(SplashFragment())
        }
    }

    fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.root.isVisible = isVisible
    }
}
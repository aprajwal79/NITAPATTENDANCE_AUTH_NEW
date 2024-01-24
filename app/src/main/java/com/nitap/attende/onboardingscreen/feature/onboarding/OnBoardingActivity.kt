package com.nitap.attende.onboardingscreen.feature.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nitap.attende.databinding.OnboardingActivityBinding
import com.nitap.attende.R
import com.nitap.attende.databinding.ActivityMainBinding
import com.nitap.attende.onboardingscreen.feature.onboarding.customView.OnBoardingView
class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      val  binding = OnboardingActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }


}

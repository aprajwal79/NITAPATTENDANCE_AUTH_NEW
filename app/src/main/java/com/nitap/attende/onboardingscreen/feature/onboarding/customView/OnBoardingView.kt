package com.nitap.attende.onboardingscreen.feature.onboarding.customView

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.nitap.attende.LoginActivity
import com.nitap.attende.onboardingscreen.domain.OnBoardingPrefManager
import com.nitap.attende.onboardingscreen.core.pageCompositePageTransformer
import com.nitap.attende.onboardingscreen.core.setParallaxTransformation
import com.nitap.attende.databinding.OnboardingViewBinding
import com.nitap.attende.onboardingscreen.feature.onboarding.OnBoardingPagerAdapter
import com.nitap.attende.onboardingscreen.feature.onboarding.entity.OnBoardingPage



@SuppressLint("NewApi")
class OnBoardingView @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val numberOfPages by lazy { OnBoardingPage.values().size }
     lateinit var prefManager: OnBoardingPrefManager


    init {
        val binding = OnboardingViewBinding.inflate(LayoutInflater.from(context), this, true)
        with(binding) {
            setUpSlider()
            addingButtonsClickListeners()
            prefManager = OnBoardingPrefManager(root.context)
        }

    }

    private fun OnboardingViewBinding.setUpSlider() {
        with(slider) {
            adapter = OnBoardingPagerAdapter()

            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }
//
//            setPageTransformer(pageCompositePageTransformer)

            addSlideChangeListener()

            val wormDotsIndicator = pageIndicator
            wormDotsIndicator.attachTo(this)
        }
    }


    private fun OnboardingViewBinding.addSlideChangeListener() {

        slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (numberOfPages > 1) {
                    val newProgress = (position + positionOffset) / (numberOfPages - 1)
                    onboardingRoot.progress = newProgress
                }
            }
        })
    }

    private fun OnboardingViewBinding.addingButtonsClickListeners() {
        nextBtn.setOnClickListener { navigateToNextSlide(slider) }
        skipBtn.setOnClickListener {
            setFirstTimeLaunchToFalse()
        }
        startBtn.setOnClickListener {
            Toast.makeText(root.context,"Select your Google Account",Toast.LENGTH_SHORT).show()
            val context = this@OnBoardingView.context
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
           // setFirstTimeLaunchToFalse()
        }
    }

    private fun setFirstTimeLaunchToFalse() {
        prefManager.isFirstTimeLaunch = false
    }

    private fun navigateToNextSlide(slider: ViewPager2?) {
        val nextSlidePos: Int = slider?.currentItem?.plus(1) ?: 0
        slider?.setCurrentItem(nextSlidePos, true)
    }


}
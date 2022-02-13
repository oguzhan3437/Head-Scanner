package com.oguzhancetin.goodpostureapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.oguzhancetin.goodpostureapp.MainActivity
import com.oguzhancetin.goodpostureapp.databinding.ActivityFragmentSlidePagerBinding
import com.oguzhancetin.goodpostureapp.adapter.SlideIntroRcAdapter
import dagger.hilt.android.AndroidEntryPoint

private const val NUM_PAGES = 3

@AndroidEntryPoint
class ScreenSlidePagerActivity : AppCompatActivity() {


    private var _binding: ActivityFragmentSlidePagerBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFragmentSlidePagerBinding.inflate(layoutInflater)

        binding.viewPager2.adapter = SlideIntroRcAdapter()
        binding.dotsIndicator.setViewPager2(binding.viewPager2)
        binding.buttonGotoMainPage.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }

        }
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
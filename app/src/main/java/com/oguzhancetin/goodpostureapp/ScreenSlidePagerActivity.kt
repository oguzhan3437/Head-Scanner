package com.oguzhancetin.goodpostureapp

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
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

        binding.viewPager2.adapter = SlideIntroRcAdapter(this)
        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if(position == 2){
                    binding.buttonGotoMainPage.visibility = View.VISIBLE
                }
            }
        })
        binding.dotsIndicator.setViewPager2(binding.viewPager2)
        binding.buttonGotoMainPage.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                it.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(it)
                finish()
            }

        }
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
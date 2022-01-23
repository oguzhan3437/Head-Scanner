package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.ActivityFragmentSlidePagerBinding
import com.oguzhancetin.goodpostureapp.slideIntro.SlideIntroRcAdapter
import dagger.hilt.android.AndroidEntryPoint

private const val NUM_PAGES = 3

@AndroidEntryPoint
class ScreenSlidePagerActivity : AppCompatActivity() {

  /*  private val viewList = mutableListOf<View>(
        layoutInflater.inflate(R.layout.slide_one, null),
        layoutInflater.inflate(R.layout.slide_one, null),
        layoutInflater.inflate(R.layout.slide_one, null),
    )*/

    private var _binding: ActivityFragmentSlidePagerBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFragmentSlidePagerBinding.inflate(layoutInflater)
        binding.viewPager2.adapter = SlideIntroRcAdapter()
        binding.dotsIndicator.setViewPager2(binding.viewPager2)

        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
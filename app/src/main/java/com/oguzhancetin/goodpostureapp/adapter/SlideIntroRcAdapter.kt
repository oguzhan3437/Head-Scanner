package com.oguzhancetin.goodpostureapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oguzhancetin.goodpostureapp.fragment.OnBoarding
import com.oguzhancetin.goodpostureapp.fragment.OnBoarding2
import com.oguzhancetin.goodpostureapp.fragment.OnBoarding3


class SlideIntroRcAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> OnBoarding()
            1 -> OnBoarding2()
            2 -> OnBoarding3()
            else -> OnBoarding()
        }

}




package com.oguzhancetin.goodpostureapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oguzhancetin.goodpostureapp.databinding.FragmentIntroBinding
import com.oguzhancetin.goodpostureapp.fragment.Onboarding1
import com.oguzhancetin.goodpostureapp.fragment.Onboarding2
import com.oguzhancetin.goodpostureapp.fragment.Onboarding3


class SlideIntroRcAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> Onboarding1()
            1 -> Onboarding2()
            2 -> Onboarding3()
            else -> Onboarding1()
        }
}




package com.oguzhancetin.goodpostureapp.fragment

import com.oguzhancetin.goodpostureapp.databinding.Onboarding1Binding
import com.oguzhancetin.goodpostureapp.databinding.Onboarding2Binding
import com.oguzhancetin.goodpostureapp.databinding.Onboarding3Binding

class OnBoarding : BaseFragment<Onboarding1Binding>() {
    override fun getViewBinding(): Onboarding1Binding {
        return Onboarding1Binding.inflate(layoutInflater)
    }
}

class OnBoarding2 : BaseFragment<Onboarding2Binding>() {
    override fun getViewBinding(): Onboarding2Binding {
        return Onboarding2Binding.inflate(layoutInflater)
    }
}

class OnBoarding3 : BaseFragment<Onboarding3Binding>() {
    override fun getViewBinding(): Onboarding3Binding {
        return Onboarding3Binding.inflate(layoutInflater)
    }
}

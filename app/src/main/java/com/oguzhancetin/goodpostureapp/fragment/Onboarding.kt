package com.oguzhancetin.goodpostureapp.fragment

import com.oguzhancetin.goodpostureapp.databinding.Onboarding1Binding
import com.oguzhancetin.goodpostureapp.databinding.Onboarding2Binding
import com.oguzhancetin.goodpostureapp.databinding.Onboarding3Binding

class Onboarding1: BaseFragment<Onboarding1Binding>(){
    override fun getViewBinding(): Onboarding1Binding {
       return  Onboarding1Binding.inflate(layoutInflater)
    }

}
class Onboarding2: BaseFragment<Onboarding2Binding>(){
    override fun getViewBinding(): Onboarding2Binding {
        return  Onboarding2Binding.inflate(layoutInflater)
    }

}
class Onboarding3: BaseFragment<Onboarding3Binding>(){
    override fun getViewBinding(): Onboarding3Binding {
        return  Onboarding3Binding.inflate(layoutInflater)
    }

}

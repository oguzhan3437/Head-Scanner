package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding> :Fragment() {

    private var _binding: VB? = null
    val binding get() = this._binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this._binding = getViewBinding()
        return this.binding.root
    }
    abstract fun getViewBinding():VB

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }


}
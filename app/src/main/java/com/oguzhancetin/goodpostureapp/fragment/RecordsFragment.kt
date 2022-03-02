package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.FragmentRecordsBinding
import com.oguzhancetin.goodpostureapp.viewmodel.MainActivityViewModel


class RecordsFragment : BaseFragment<FragmentRecordsBinding>() {
    private val model: MainActivityViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun getViewBinding() = FragmentRecordsBinding.inflate(layoutInflater)



}
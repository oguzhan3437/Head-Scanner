package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.FragmentDashBoardBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_dash_board.*
import kotlinx.android.synthetic.main.fragment_dash_board.view.*


class DashBoardFragment : BaseFragment<FragmentDashBoardBinding>() {



    private fun goToCamera() {
        findNavController().navigate(DashBoardFragmentDirections.actionDashBoardFragmentToMainFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewCamera.setOnClickListener {goToCamera()}
    }

    override fun getViewBinding(): FragmentDashBoardBinding {
        return FragmentDashBoardBinding.inflate(layoutInflater)
    }


}
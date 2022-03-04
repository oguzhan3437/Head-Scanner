package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.adapter.TipsViewPagerAdapter
import com.oguzhancetin.goodpostureapp.databinding.FragmentDashBoardBinding
import kotlinx.android.synthetic.main.fragment_dash_board.*
import kotlinx.android.synthetic.main.fragment_dash_board.view.*
import me.relex.circleindicator.CircleIndicator


class DashBoardFragment : BaseFragment<FragmentDashBoardBinding>() {




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePager()
        binding.imageViewCamera.setOnClickListener { goToCamera() }
        binding.imageViewGallery.setOnClickListener { goToGallery() }
        binding.imageViewExercise.setOnClickListener { goToExercises() }
        binding.imageViewRecords.setOnClickListener { goToRecords() }
    }

    private fun initializePager() {
        //tips images
        val tipImageList = listOf(
            R.drawable.tips1,
            R.drawable.tips1,
            R.drawable.tips1,
        )
        val adapter = TipsViewPagerAdapter(tipImageList)
        binding.pager.adapter = adapter
        binding.indicator.apply {
            tintIndicator(
                ContextCompat.getColor(requireContext(), R.color.black),
                ContextCompat.getColor(requireContext(), R.color.primary)
            )
            setViewPager(binding.pager)
        }
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver);
    }

    private fun goToRecords() {
        findNavController()
            .navigate(DashBoardFragmentDirections.actionDashBoardFragmentToRecordsFragment())
    }

    private fun goToExercises() {
        findNavController()
            .navigate(DashBoardFragmentDirections.actionDashBoardFragmentToExercisesFragment())
    }

    private fun goToGallery() {
        findNavController()
            .navigate(DashBoardFragmentDirections.actionDashBoardFragmentToGalleryFragment())
    }
    private fun goToCamera() {
        findNavController()
            .navigate(DashBoardFragmentDirections.actionDashBoardFragmentToMainFragment())
    }

    override fun getViewBinding(): FragmentDashBoardBinding {
        return FragmentDashBoardBinding.inflate(layoutInflater)
    }


}
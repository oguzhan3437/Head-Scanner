package com.oguzhancetin.goodpostureapp.fragment


import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.adapter.TipsViewPagerAdapter
import com.oguzhancetin.goodpostureapp.databinding.FragmentDashBoardBinding
import kotlinx.coroutines.*



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
            R.drawable.tip8,
            R.drawable.tip6,
            R.drawable.tip7
        )
        val adapter = TipsViewPagerAdapter(tipImageList)
        binding.pager.adapter = adapter
        binding.indicator.apply {
            tintIndicator(
                ContextCompat.getColor(requireContext(), R.color.primary),
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            setViewPager(binding.pager)
        }
        adapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)
        dragPager()
    }

    private fun dragPager() {
        this.viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            var page = 1
            while (true) {
                if (page > 2) {
                    page = 0
                }
                delay(3000)
                withContext(Dispatchers.Main) {

                    binding.pager.currentItem = page
                }
                page++
            }
        }
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

    override fun getViewBinding(): FragmentDashBoardBinding =
        FragmentDashBoardBinding.inflate(layoutInflater)
}
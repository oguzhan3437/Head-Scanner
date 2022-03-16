package com.oguzhancetin.goodpostureapp.fragment

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.adapter.TipsViewPagerAdapter
import com.oguzhancetin.goodpostureapp.databinding.FragmentDashBoardBinding
import kotlinx.android.synthetic.main.fragment_dash_board.*
import kotlinx.android.synthetic.main.fragment_dash_board.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import me.relex.circleindicator.CircleIndicator
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


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
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver)
        dragPager()
    }

    private fun dragPager() {
        this.viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            var page = 1
            while (true) {
                if (page > 1) {
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
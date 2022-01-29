package com.oguzhancetin.goodpostureapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzhancetin.goodpostureapp.adapter.ExerciseRcAdapter
import com.oguzhancetin.goodpostureapp.databinding.FragmentExercisesBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentExercisesBinding.*
import com.oguzhancetin.goodpostureapp.fragment.BaseFragment
import com.oguzhancetin.goodpostureapp.model.Exercise


class ExercisesFragment : BaseFragment<FragmentExercisesBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exerciseAdapter = ExerciseRcAdapter(
            listOf<Exercise>(
                Exercise(1,"e6",R.drawable.common_full_open_on_phone),
                Exercise(1,"e5",R.drawable.common_full_open_on_phone),
                Exercise(1,"e4",R.drawable.common_full_open_on_phone),
                Exercise(1,"e3",R.drawable.common_full_open_on_phone),
                Exercise(1,"e2",R.drawable.common_full_open_on_phone),
            )
        )
        binding.exercisesRc.apply {
            adapter = exerciseAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
    }

    override fun getViewBinding() = inflate(layoutInflater)


}
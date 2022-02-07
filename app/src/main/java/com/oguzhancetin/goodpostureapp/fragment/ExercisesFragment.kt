package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.adapter.ExerciseRcAdapter
import com.oguzhancetin.goodpostureapp.databinding.FragmentExercisesBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentExercisesBinding.*
import com.oguzhancetin.goodpostureapp.model.Exercise


class ExercisesFragment : BaseFragment<FragmentExercisesBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exerciseAdapter = ExerciseRcAdapter(
            listOf<Exercise>(
                Exercise(1,"e6", R.drawable.common_full_open_on_phone),
                Exercise(1,"e5", R.drawable.common_full_open_on_phone),
                Exercise(1,"e4", R.drawable.common_full_open_on_phone),
                Exercise(1,"e3", R.drawable.common_full_open_on_phone),
                Exercise(1,"e2", R.drawable.common_full_open_on_phone),
            )
        )
        binding.exercisesRc.apply {
            adapter = exerciseAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getViewBinding() = inflate(layoutInflater)


}
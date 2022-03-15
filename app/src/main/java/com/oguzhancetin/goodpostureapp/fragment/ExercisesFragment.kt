package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.adapter.ExerciseRcAdapter
import com.oguzhancetin.goodpostureapp.databinding.FragmentExercisesBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentExercisesBinding.*
import com.oguzhancetin.goodpostureapp.data.model.Exercise


class ExercisesFragment : BaseFragment<FragmentExercisesBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exerciseAdapter = ExerciseRcAdapter(
            listOf<Exercise>(
                Exercise(1,getString(R.string.exercise_name_1),getString(R.string.exercise_head_1),getString(R.string.exercise_explanation_1), R.drawable.exercise1),
                Exercise(2,getString(R.string.exercise_name_2),getString(R.string.exercise_head_2),getString(R.string.exercise_explanation_2), R.drawable.exercise2),
                Exercise(3,getString(R.string.exercise_name_3),getString(R.string.exercise_head_3),getString(R.string.exercise_explanation_3), R.drawable.exercise3),
                Exercise(4,getString(R.string.exercise_name_4),getString(R.string.exercise_head_4),getString(R.string.exercise_explanation_4), R.drawable.exercise4),
                Exercise(5,getString(R.string.exercise_name_5),getString(R.string.exercise_head_5),getString(R.string.exercise_explanation_5), R.drawable.exercise5),

            )
        )
        binding.exercisesRc.apply {
            adapter = exerciseAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getViewBinding() = inflate(layoutInflater)


}
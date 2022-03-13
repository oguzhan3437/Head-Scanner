package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
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
                Exercise(1,getString(R.string.exercise_name_1),getString(R.string.exercise_head_1),getString(R.string.exercise_explanation_1), R.drawable.e1),
                Exercise(1,getString(R.string.exercise_name_2),getString(R.string.exercise_head_2),getString(R.string.exercise_explanation_2), R.drawable.e2),
                Exercise(1,getString(R.string.exercise_name_3),getString(R.string.exercise_head_3),getString(R.string.exercise_explanation_3), R.drawable.e3),
                Exercise(1,getString(R.string.exercise_name_4),getString(R.string.exercise_head_4),getString(R.string.exercise_explanation_4), R.drawable.e4),
                Exercise(1,getString(R.string.exercise_name_5),getString(R.string.exercise_head_5),getString(R.string.exercise_explanation_5), R.drawable.e5),

            )
        )
        binding.exercisesRc.apply {
            adapter = exerciseAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getViewBinding() = inflate(layoutInflater)


}
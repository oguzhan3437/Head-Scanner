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
                Exercise(1,"e6", R.drawable.e1),
                Exercise(1,"e5", R.drawable.e2),
                Exercise(1,"e4", R.drawable.e3),
                Exercise(1,"e3", R.drawable.e4),
                Exercise(1,"e2", R.drawable.e5),
            )
        )
        binding.exercisesRc.apply {
            adapter = exerciseAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getViewBinding() = inflate(layoutInflater)


}
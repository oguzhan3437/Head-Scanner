package com.oguzhancetin.goodpostureapp.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.FragmentExerciseDetailBinding



class ExerciseDetailFragment : BaseFragment<FragmentExerciseDetailBinding>() {
    private val args:ExerciseDetailFragmentArgs by navArgs()

    override fun getViewBinding(): FragmentExerciseDetailBinding {
        return FragmentExerciseDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exercise = args.exercise
        binding.txtExerciseHeaderExplanation.text = exercise.headerExplanation
        binding.txtExerciseExplanation.text = exercise.explanation
        binding.imgExercise.transitionName = exercise.imageLocation.toString()
        Glide.with(binding.imgExercise).load(exercise.imageLocation).into(binding.imgExercise)
    }


}
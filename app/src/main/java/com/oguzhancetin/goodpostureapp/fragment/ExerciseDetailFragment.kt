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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExerciseDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        binding.imgExercise.transitionName = exercise.imageLocation.toString()
        Glide.with(binding.imgExercise).load(exercise.imageLocation).into(binding.imgExercise)
    }


}
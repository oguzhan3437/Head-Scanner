package com.oguzhancetin.goodpostureapp.adapter

import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oguzhancetin.goodpostureapp.databinding.ExerciseCardBinding
import com.oguzhancetin.goodpostureapp.data.model.Exercise
import com.oguzhancetin.goodpostureapp.fragment.ExercisesFragmentDirections

class ExerciseViewHolder(
    private val cardBinding: ExerciseCardBinding,
) :
    RecyclerView.ViewHolder(cardBinding.root) {


    fun bind(exercise: Exercise) {
        cardBinding.cardImageViewExercise.apply {
            transitionName = exercise.imageLocation.toString()
            Glide.with(this)
                .load(exercise.imageLocation)
                .into(this)
        }
        itemView.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                Pair(cardBinding.cardImageViewExercise, exercise.imageLocation.toString())
            )
            it.findNavController()
                .navigate(
                    ExercisesFragmentDirections
                        .actionExercisesFragmentToExerciseDetailFragment(exercise),extras
                )
        }

    }
}
package com.oguzhancetin.goodpostureapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oguzhancetin.goodpostureapp.databinding.ExerciseCardBinding
import com.oguzhancetin.goodpostureapp.data.model.Exercise

class ExerciseViewHolder(private val cardBinding: ExerciseCardBinding) :
    RecyclerView.ViewHolder(cardBinding.root) {

    fun bind(exercise: Exercise) {
        Glide.with(cardBinding.cardImageViewExercise)
            .load(exercise.imageLocation)
            .into(cardBinding.cardImageViewExercise)
    }
}
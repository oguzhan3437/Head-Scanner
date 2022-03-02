package com.oguzhancetin.goodpostureapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzhancetin.goodpostureapp.databinding.ExerciseCardBinding
import com.oguzhancetin.goodpostureapp.data.model.Exercise

class ExerciseRcAdapter(private val exercises:List<Exercise>) : RecyclerView.Adapter<ExerciseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ExerciseCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size
}
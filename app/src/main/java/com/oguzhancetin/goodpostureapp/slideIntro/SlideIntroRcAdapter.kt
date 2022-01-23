package com.oguzhancetin.goodpostureapp.slideIntro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.CardGalleryImageBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentIntroBinding
import com.oguzhancetin.goodpostureapp.gallery.GalleryViewHolder

class SlideIntroRcAdapter : RecyclerView.Adapter<SlideIntroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideIntroViewHolder {
        val binding = FragmentIntroBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  SlideIntroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideIntroViewHolder, position: Int) {

    }

    override fun getItemCount(): Int  = 3
}
package com.oguzhancetin.goodpostureapp.gallery

import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oguzhancetin.goodpostureapp.R
import com.oguzhancetin.goodpostureapp.databinding.CardGalleryImageBinding

class GalleryViewHolder(
    private val binding: CardGalleryImageBinding,
    private val onCLick: (Uri) -> (Unit)
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uri: Uri) {
        Glide
            .with(binding.root)
            .load(uri)
            .centerCrop()
            .into(binding.imgImage)

        binding.imgImage.setOnClickListener {
            onCLick(uri)
        }
    }


}

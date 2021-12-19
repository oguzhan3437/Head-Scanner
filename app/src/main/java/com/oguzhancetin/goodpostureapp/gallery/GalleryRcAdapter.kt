package com.oguzhancetin.goodpostureapp.gallery

import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oguzhancetin.goodpostureapp.databinding.ActivityMainBinding
import com.oguzhancetin.goodpostureapp.databinding.CardGalleryImageBinding


class GalleryRcAdapter(private val uriSet: List<Uri>,private val onCLickItem:(Uri)->(Unit)) :
    RecyclerView.Adapter<GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {

        val binding = CardGalleryImageBinding.inflate(LayoutInflater.from(parent.context))
        return GalleryViewHolder(binding,onCLickItem)


    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(uriSet[position])
    }

    override fun getItemCount() = uriSet.size


}

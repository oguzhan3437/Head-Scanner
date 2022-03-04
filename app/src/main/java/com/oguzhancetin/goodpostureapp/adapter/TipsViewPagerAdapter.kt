package com.oguzhancetin.goodpostureapp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.oguzhancetin.goodpostureapp.databinding.FragmentRecordsBinding
import com.oguzhancetin.goodpostureapp.databinding.CardTipBinding

class TipsViewPagerAdapter(@DrawableRes private val  imageList:List<Int>) :
    RecyclerView.Adapter<TipsViewPagerAdapter.TipsViewHolder>() {

    class TipsViewHolder(private val binding: CardTipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(@DrawableRes image: Int) {
            Glide.with(binding.imageViewTip).load(image).into(binding.imageViewTip)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        return TipsViewHolder(CardTipBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size


}
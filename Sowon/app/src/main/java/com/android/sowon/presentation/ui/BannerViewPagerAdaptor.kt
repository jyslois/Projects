package com.android.sowon.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.sowon.databinding.ItemBannerBinding

class BannerViewPagerAdaptor(var eventBannerList: ArrayList<Int>) : RecyclerView.Adapter<BannerViewPagerAdaptor.BannerViewHolder>() {

    inner class BannerViewHolder(binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        val bannerImage = binding.bannerImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bannerImage.setImageResource(eventBannerList[position])
    }

    override fun getItemCount(): Int = eventBannerList.size

}
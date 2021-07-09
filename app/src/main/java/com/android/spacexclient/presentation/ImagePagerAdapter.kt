package com.android.spacexclient.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.spacexclient.databinding.ViewpagerItemBinding
import com.bumptech.glide.Glide

class ImagePagerAdapter(private val items:List<String>): RecyclerView.Adapter<RocketImageHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketImageHolder {
        return RocketImageHolder(
            ViewpagerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: RocketImageHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item)
            .fitCenter()
            .into(holder.binding.rocketImage);
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}

class RocketImageHolder(val binding: ViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root)

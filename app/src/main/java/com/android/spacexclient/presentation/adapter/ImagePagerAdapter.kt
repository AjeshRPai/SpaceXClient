package com.android.spacexclient.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.spacexclient.R
import com.android.spacexclient.databinding.ViewpagerItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL


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
        val imageView = holder.binding.rocketImage

        val options = RequestOptions()
            .fitCenter()
            .override(SIZE_ORIGINAL,240)
            .placeholder(R.drawable.ic_baseline_cloud_download_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(imageView.context)
            .load(item)
            .apply(options)
            .into(imageView)
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}

class RocketImageHolder(val binding: ViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root)

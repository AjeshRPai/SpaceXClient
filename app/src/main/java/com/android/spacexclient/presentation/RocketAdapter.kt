package com.android.spacexclient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.spacexclient.databinding.RocketListItemBinding
import com.android.spacexclient.domain.RocketModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class RocketAdapter() : ListAdapter<RocketModel, RocketViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketViewHolder {
        return RocketViewHolder(
            RocketListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: RocketViewHolder, position: Int) {
        val currentItem: RocketModel = getItem(position)

        Glide.with(holder.itemView.context)
            .load(currentItem.flickrImages[0])
            .fitCenter()
            .into(holder.binding.rocketImage);


        holder.binding.rocketName.text = currentItem.name
        holder.binding.country.text =currentItem.country
        holder.binding.engines.text = holder.itemView.resources.getString(R.string.engine_count,currentItem.engines)
    }
}

class RocketViewHolder(val binding: RocketListItemBinding) : RecyclerView.ViewHolder(binding.root)

class DiffCallback : DiffUtil.ItemCallback<RocketModel>() {
    override fun areItemsTheSame(oldItem: RocketModel, newItem: RocketModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RocketModel, newItem: RocketModel): Boolean {
        return oldItem == newItem
    }

}

package com.android.spacexclient.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.spacexclient.R
import com.android.spacexclient.databinding.RocketListItemBinding
import com.android.spacexclient.domain.model.RocketModel
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber

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
        val binding = holder.binding

        Timber.e(currentItem.toString())

        binding.rocketImages.adapter = ImagePagerAdapter(currentItem.flickrImages)
        if (currentItem.flickrImages.count() > 1)
        {
            TabLayoutMediator(binding.tabLayout, binding.rocketImages) { _, _ -> }.attach()
            binding.tabLayout.isVisible = true
        }

        binding.rocketName.text = currentItem.name
        binding.country.text = holder.itemView.resources.getString(R.string.rocket_country, currentItem.country)
        binding.engines.text =
            holder.itemView.resources.getString(R.string.engine_count, currentItem.engines)
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

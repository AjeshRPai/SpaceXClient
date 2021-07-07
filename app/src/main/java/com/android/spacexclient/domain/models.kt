package com.android.spacexclient.domain


data class RocketModel(
    val id: String,
    val name: String,
    val country: String,
    val active: Boolean,
    val flickrImages: List<String>,
    val engines: Int
)
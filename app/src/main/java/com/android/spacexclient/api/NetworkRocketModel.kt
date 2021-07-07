package com.android.spacexclient.api

import com.google.gson.annotations.SerializedName

data class NetworkRocketModel(
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("country")
    val country: String,
    @SerializedName("engines")
    val engines: Engines,
    @SerializedName("flickr_images")
    val flickrImages: List<String>,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)

data class Engines(
    @SerializedName("number")
    val number: Int
)
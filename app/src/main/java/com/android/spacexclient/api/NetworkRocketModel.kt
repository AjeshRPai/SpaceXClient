package com.android.spacexclient.api

import com.google.gson.annotations.SerializedName

data class NetworkRocketModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("engines")
    val engines: Engines,
    @SerializedName("flickr_images")
    val flickrImages: List<String>,
    @SerializedName("first_flight")
    val first_flight:String
)

data class Engines(
    @SerializedName("number")
    val number: Int
)
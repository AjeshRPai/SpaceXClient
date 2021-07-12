package com.android.spacexclient.data.api

import com.android.spacexclient.data.api.model.NetworkRocketModel
import io.reactivex.Single
import retrofit2.http.GET

interface RocketApi {

    @GET("v4/rockets")
    fun getRockets(): Single<List<NetworkRocketModel>>

}
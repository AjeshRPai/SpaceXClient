package com.android.spacexclient.api

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface RocketApi {

    @GET("v4/rockets")
    fun getRockets(): Single<List<NetworkRocketModel>>

}
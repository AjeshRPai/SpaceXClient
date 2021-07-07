package com.android.spacexclient.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface RocketApi {

    @GET("v4/rockets")
    fun getRockets(): Observable<List<NetworkRocketModel>>

}
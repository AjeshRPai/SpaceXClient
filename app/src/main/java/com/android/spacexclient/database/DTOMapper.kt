package com.android.spacexclient.database

import com.android.spacexclient.api.NetworkRocketModel

fun convertToDbModel(rocketModel: NetworkRocketModel): LocalRocketModel {
    with(rocketModel) {
        return LocalRocketModel(
            id = id,
            name = name,
            country = country,
            active = active,
            flickrImages = flickrImages,
            engines = engines.number
        )
    }
}
fun convertToDbModel(movies: List<NetworkRocketModel>): List<LocalRocketModel> {
    return movies.map {
        convertToDbModel(it)
    }
}




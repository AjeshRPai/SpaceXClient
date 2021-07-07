package com.android.spacexclient.domain

import com.android.spacexclient.database.LocalRocketModel

interface DomainMapper<in T, out A> {
    fun map(model: T): A
}

object Mapper : DomainMapper<List<LocalRocketModel>, List<RocketModel>> {
    override fun map(model: List<LocalRocketModel>): List<RocketModel> {
        return model.map { convertToRocketModel(it) }
    }
}

fun convertToRocketModel(model: LocalRocketModel): RocketModel {
    with(model) {
        return RocketModel(
            id = id,
            name = name,
            country = country,
            active = active,
            flickrImages = flickrImages,
            engines = engines
        )
    }
}
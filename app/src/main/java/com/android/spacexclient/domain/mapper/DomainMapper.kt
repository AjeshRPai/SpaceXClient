package com.android.spacexclient.domain.mapper

import com.android.spacexclient.data.database.model.LocalRocketModel
import com.android.spacexclient.domain.model.RocketModel

interface DomainMapper<in T, out A> {
    fun map(model: T): A
}

class RocketDomainMapper : DomainMapper<LocalRocketModel, RocketModel> {

    override fun map(model: LocalRocketModel): RocketModel {
        return convertToRocketModel(model)
    }

    private fun convertToRocketModel(model: LocalRocketModel): RocketModel {
        with(model) {
            return RocketModel(
                id = id,
                name = name,
                country = country,
                active = active,
                flickrImages = flickrImages,
                engines = engines,
                year = yearOfLaunch
            )
        }
    }
}


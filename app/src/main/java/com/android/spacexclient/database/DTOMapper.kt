package com.android.spacexclient.database

import com.android.spacexclient.api.NetworkRocketModel
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

interface DTOMapper<in T, out A> {
    fun map(model: T): A
}

class RocketDtoMapper : DTOMapper<NetworkRocketModel,LocalRocketModel> {

    companion object {
        private const val NetworkDatePattern = "yyyy-MM-dd"
        private const val LocalDatePattern = "yyyy"
    }

    override fun map(model: NetworkRocketModel): LocalRocketModel {
        return convert(model)
    }

    private fun convert(rocketModel: NetworkRocketModel): LocalRocketModel {
        with(rocketModel) {
            return LocalRocketModel(
                id = id,
                name = name,
                country = country,
                active = active,
                flickrImages = flickrImages,
                engines = engines.number,
                yearOfLaunch = convertDateString(first_flight).orEmpty()
            )
        }
    }


    private fun convertDateString(year: String): String? {
        val format:DateFormat = SimpleDateFormat(NetworkDatePattern, Locale.getDefault())
        try {
            val date = format.parse(year)
            val df: DateFormat = SimpleDateFormat(LocalDatePattern, Locale.getDefault())
            return date?.run {
                df.format(date)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }
}









package com.android.spacexclient

import com.android.spacexclient.api.NetworkRocketModel
import com.android.spacexclient.api.RocketApi
import com.android.spacexclient.database.LocalRocketModel
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.database.RocketDtoMapper
import com.android.spacexclient.domain.RocketDomainMapper
import com.android.spacexclient.domain.RocketModel
import com.android.spacexclient.presentation.utils.Query
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class RocketRepository @Inject constructor(
    private val rocketDao: RocketDao,
    private val rocketApi: RocketApi,
    private val rocketDomainMapper: RocketDomainMapper,
    private val rocketDtoMapper: RocketDtoMapper,
) {

    fun getRockets(query: Query): Single<Result<List<RocketModel>>> {
        return refreshDatabase()
            .onErrorResumeNext { throwable ->
                if (throwable is IOException) {
                    return@onErrorResumeNext getRocketsFromDb()
                }
                return@onErrorResumeNext Single.error(throwable)
            }
            .map { list -> list.filter { if (query.onlyActive) it.active else true } }
            .map { list ->
                if (list.isNotEmpty()) mapToDomainResult(list)
                else Result.failure(Throwable("No Results"))
            }
            .onErrorReturn { Result.failure(it) }
    }

    fun refreshRockets(): Single<Result<List<RocketModel>>> {
        return refreshDatabase()
            .map { list -> mapToDomainResult(list) }
            .onErrorReturn { Result.failure(it) }
    }

    private fun refreshDatabase(): Single<List<LocalRocketModel>> {
        return getRocketsFromApi()
            .map { list -> mapToLocalModel(list) }
            .doOnSuccess { list -> rocketDao.insertAll(list) }
    }

    private fun mapToLocalModel(list: List<NetworkRocketModel>): List<LocalRocketModel> =
        list.map { rocketDtoMapper.map(it) }

    private fun mapToDomainResult(list: List<LocalRocketModel>): Result<List<RocketModel>> =
        Result.success(list.map { rocketDomainMapper.map(it) })


    private fun getRocketsFromApi(): Single<List<NetworkRocketModel>> {
        return rocketApi.getRockets()
    }

    private fun getRocketsFromDb(): Single<List<LocalRocketModel>> {
        return rocketDao.getRockets()
    }


}


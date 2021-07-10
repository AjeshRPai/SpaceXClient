package com.android.spacexclient

import com.android.spacexclient.api.NetworkRocketModel
import com.android.spacexclient.api.RocketApi
import com.android.spacexclient.database.LocalRocketModel
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.database.RocketDtoMapper
import com.android.spacexclient.domain.RocketDomainMapper
import com.android.spacexclient.domain.RocketModel
import com.android.spacexclient.presentation.utils.Query
import io.reactivex.Observable
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
        return getRocketsFromApi()
            .map { list -> list.map { rocketDtoMapper.map(it) } }
            .onErrorResumeNext { throwable ->
                if (throwable is IOException) {
                    return@onErrorResumeNext rocketDao.getRockets()
                }
                return@onErrorResumeNext Single.error(throwable)
            }
            .map { list -> list.filter { if (query.onlyActive) it.active else true } }
            .map { list -> Result.success(list.map { rocketDomainMapper.map(it) }) }
            .onErrorReturn { Result.failure(it) }
    }

    fun refreshRockets(): Single<Result<List<RocketModel>>> {
        return refreshDatabase()
            .map { list -> Result.success(list.map { rocketDomainMapper.map(it) }) }
            .onErrorReturn { Result.failure(it) }
    }

    private fun refreshDatabase(): Single<List<LocalRocketModel>> {
        return getRocketsFromApi()
            .map { list -> list.map { rocketDtoMapper.map(it) } }
            .doOnSuccess { list -> rocketDao.insertAll(list) }
    }

    private fun getRocketsFromApi(): Single<List<NetworkRocketModel>> {
        return rocketApi.getRockets()

    }

    private fun getRocketsFromDb(): Single<List<LocalRocketModel>> {
        return rocketDao.getRockets()
            .subscribeOn(Schedulers.io())
    }


}


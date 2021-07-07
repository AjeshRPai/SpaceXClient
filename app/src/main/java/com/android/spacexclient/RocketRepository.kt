package com.android.spacexclient

import com.android.spacexclient.api.RocketApi
import com.android.spacexclient.database.LocalRocketModel
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.database.convertToDbModel
import com.android.spacexclient.domain.DomainMapper
import com.android.spacexclient.domain.RocketModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RocketRepository @Inject constructor(
    private val rocketDao: RocketDao,
    private val rocketApi: RocketApi,
    private val dataMapper: DomainMapper<List<LocalRocketModel>, List<RocketModel>>
) {

    fun getRockets(): Single<Result<List<RocketModel>>> {
        return getRocketsFromDb().concatWith(getRocketsFromApi())
            .map {
                Result.success(dataMapper.map(it))
            }.firstOrError()
    }

    private fun getRocketsFromApi(): Observable<List<LocalRocketModel>> {
        return rocketApi.getRockets()
            .map { convertToDbModel(it) }
            .doOnNext {
                rocketDao.insertAll(it).andThen(Observable.just(it))
            }
    }

    private fun getRocketsFromDb(): Observable<List<LocalRocketModel>> {
        return rocketDao.getRockets()
    }

    fun getActiveRockets(): Observable<Result<List<RocketModel>>> {
        return rocketDao
            .getActiveRockets()
            .map { Result.success(dataMapper.map(it)) }
            .onErrorReturn { Result.failure(it) }
    }

    fun refreshRockets(): Observable<Result<List<RocketModel>>> {
        return getRocketsFromApi()
            .map {
                Result.success(dataMapper.map(it))
            }.onErrorReturn { Result.failure(it) }
    }
}


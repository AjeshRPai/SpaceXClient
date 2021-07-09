package com.android.spacexclient

import com.android.spacexclient.api.RocketApi
import com.android.spacexclient.database.LocalRocketModel
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.database.convertToDbModel
import com.android.spacexclient.domain.DomainMapper
import com.android.spacexclient.domain.RocketModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RocketRepository @Inject constructor(
    private val rocketDao: RocketDao,
    private val rocketApi: RocketApi,
    private val dataMapper: DomainMapper<List<LocalRocketModel>, List<RocketModel>>
) {

    fun getRockets(): Observable<Result<List<RocketModel>>> {
        return getRocketsFromDb().concatWith(getRocketsFromApi())
            .distinctUntilChanged()
            .map {
                Result.success(dataMapper.map(it))
            }.onErrorReturn { Result.failure(it) }
    }

    fun getActiveRockets(): Observable<Result<List<RocketModel>>> {
        return rocketDao
            .getActiveRockets()
            .map { Result.success(dataMapper.map(it)) }
            .onErrorReturn { Result.failure(it) }
    }

    fun refreshRockets(): Observable<Result<List<RocketModel>>> {
        return rocketApi.getRockets()
            .map { convertToDbModel(it) }
            .doOnNext {
                rocketDao.insertAll(it).andThen(Observable.just(it))
            }.map {
                Result.success(dataMapper.map(it))
            }.onErrorReturn { Result.failure(it) }
    }

    private fun getRocketsFromApi(): Observable<List<LocalRocketModel>> {
        return rocketApi.getRockets()
            .map { convertToDbModel(it) }
            .doOnNext {
                rocketDao.insertAll(it).andThen(Observable.just(it))
            }.onErrorResumeWith {
                rocketDao.getRockets()
            }
    }

    private fun getRocketsFromDb(): Observable<List<LocalRocketModel>> {
        return rocketDao.getRockets().filter { it.isNotEmpty() }
    }


}


package com.android.spacexclient.data.repository

import com.android.spacexclient.data.api.model.NetworkRocketModel
import com.android.spacexclient.data.api.RocketApi
import com.android.spacexclient.data.database.RocketDao
import com.android.spacexclient.data.mapper.RocketDtoMapper
import com.android.spacexclient.data.database.model.LocalRocketModel
import com.android.spacexclient.domain.mapper.RocketDomainMapper
import com.android.spacexclient.domain.model.RocketModel
import com.android.spacexclient.domain.model.Query
import java.lang.NullPointerException
import javax.inject.Inject

class RocketRepository @Inject constructor(
    private val rocketDao: RocketDao,
    private val rocketApi: RocketApi,
    private val rocketDomainMapper: RocketDomainMapper,
    private val rocketDtoMapper: RocketDtoMapper,
) {

    suspend fun getRockets(query: Query): Result<List<RocketModel>> {
        var rocketRefreshResponse = refreshRocketList()
        if (rocketRefreshResponse.isSuccess &&
            !rocketRefreshResponse.getOrNull().isNullOrEmpty()
        ) {
            val rocketList = rocketRefreshResponse.getOrNull()
            val filtered = rocketList?.filter { if (query.onlyActive) it.active else true }
            if (filtered.isNullOrEmpty()) {
                return Result.failure(NullPointerException())
            }
            return Result.success(filtered)
        } else {
            val list = getRocketsFromDb()
            return if (list.isEmpty()) {
                Result.failure(NullPointerException())
            } else {
                Result.success(mapToDomainModel(list))
            }
        }
    }

    suspend fun refreshRocketList(): Result<List<RocketModel>> {
        val apiResponse = getRocketsFromApi()
        if (apiResponse.isNotEmpty()) {
            refreshDatabase(apiResponse)
            val dbResponse = getRocketsFromDb()
            return Result.success(mapToDomainModel(dbResponse))
        }
        return Result.failure(NullPointerException())
    }


    private suspend fun refreshDatabase(list: List<NetworkRocketModel>) {
        val localList = mapToLocalModel(list)
        rocketDao.insertAll(localList)
    }

    private fun mapToLocalModel(list: List<NetworkRocketModel>): List<LocalRocketModel> =
        list.map { rocketDtoMapper.map(it) }

    private fun mapToDomainModel(list: List<LocalRocketModel>): List<RocketModel> =
        list.map { rocketDomainMapper.map(it) }

    private suspend fun getRocketsFromApi(): List<NetworkRocketModel> {
        return rocketApi.getRockets()
    }

    private suspend fun getRocketsFromDb(): List<LocalRocketModel> {
        return rocketDao.getRockets()
    }


}


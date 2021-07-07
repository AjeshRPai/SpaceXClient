package com.android.spacexclient.domain

import com.android.spacexclient.RocketRepository
import com.android.spacexclient.database.LocalRocketModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetRocketsUseCase @Inject constructor(
    private val repository: RocketRepository
) {
    fun getRockets(): Single<Result<List<RocketModel>>> {
        return repository.getRockets()
    }
}

class GetActiveRocketsUseCase(
    private val repository: RocketRepository,
) {
    fun getActiveRockets(): Observable<Result<List<RocketModel>>> {
        return repository.getActiveRockets()
    }
}

class RefreshRocketsUseCase(
    private val repository: RocketRepository,
){
    fun refreshRocketEntries(): Observable<Result<List<RocketModel>>> {
        return repository.refreshRockets()
    }
}


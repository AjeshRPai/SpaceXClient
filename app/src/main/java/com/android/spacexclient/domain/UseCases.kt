package com.android.spacexclient.domain

import com.android.spacexclient.RocketRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface GetRocketsUseCase {
    operator fun invoke(): Single<Result<List<RocketModel>>>
}

class GetRocketsUseCaseImpl @Inject constructor(
    private val repository: RocketRepository
):GetRocketsUseCase {
    override fun invoke(): Single<Result<List<RocketModel>>> {
        return repository.getRockets()
    }
}

interface GetActiveRocketsUseCase {
    operator fun invoke(): Observable<Result<List<RocketModel>>>
}

class GetActiveRocketsUseCaseImpl @Inject constructor(
    private val repository: RocketRepository,
):GetActiveRocketsUseCase {
    override fun invoke(): Observable<Result<List<RocketModel>>> {
        return repository.getActiveRockets()
    }
}

interface RefreshRocketsUseCase {
    operator fun invoke(): Observable<Result<List<RocketModel>>>
}

class RefreshRocketsUseCaseImpl @Inject constructor(
    private val repository: RocketRepository,
):RefreshRocketsUseCase{
    override fun invoke(): Observable<Result<List<RocketModel>>> {
        return repository.refreshRockets()
    }
}


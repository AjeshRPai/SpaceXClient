package com.android.spacexclient.domain

import com.android.spacexclient.RocketRepository
import com.android.spacexclient.presentation.utils.Query
import io.reactivex.Single
import javax.inject.Inject

interface GetRocketsUseCase {
    operator fun invoke(query: Query): Single<Result<List<RocketModel>>>
}

class GetRocketsUseCaseImpl @Inject constructor(
    private val repository: RocketRepository
):GetRocketsUseCase {
    override fun invoke(query: Query): Single<Result<List<RocketModel>>> {
        return repository.getRockets(query)
    }
}


interface RefreshRocketsUseCase {
    operator fun invoke(): Single<Result<List<RocketModel>>>
}

class RefreshRocketsUseCaseImpl @Inject constructor(
    private val repository: RocketRepository,
):RefreshRocketsUseCase{
    override fun invoke(): Single<Result<List<RocketModel>>> {
        return repository.refreshRockets()
    }
}


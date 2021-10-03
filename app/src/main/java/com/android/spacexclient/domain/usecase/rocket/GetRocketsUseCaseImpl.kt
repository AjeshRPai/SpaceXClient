package com.android.spacexclient.domain.usecase.rocket

import com.android.spacexclient.data.repository.RocketRepository
import com.android.spacexclient.domain.model.RocketModel
import com.android.spacexclient.domain.model.Query
import io.reactivex.Single
import javax.inject.Inject

class GetRocketsUseCaseImpl @Inject constructor(
    private val repository: RocketRepository
): GetRocketsUseCase {
    override suspend fun invoke(query: Query): Result<List<RocketModel>> {
        return repository.getRockets(query)
    }
}
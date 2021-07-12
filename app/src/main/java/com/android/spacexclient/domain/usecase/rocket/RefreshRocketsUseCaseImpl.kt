package com.android.spacexclient.domain.usecase.rocket

import com.android.spacexclient.data.repository.RocketRepository
import com.android.spacexclient.domain.model.RocketModel
import io.reactivex.Single
import javax.inject.Inject

class RefreshRocketsUseCaseImpl @Inject constructor(
    private val repository: RocketRepository,
): RefreshRocketsUseCase {
    override fun invoke(): Single<Result<List<RocketModel>>> {
        return repository.refreshRockets()
    }
}
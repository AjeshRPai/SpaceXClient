package com.android.spacexclient.domain.usecase.rocket

import com.android.spacexclient.domain.model.RocketModel
import io.reactivex.Single

interface RefreshRocketsUseCase {
    suspend operator fun invoke(): Result<List<RocketModel>>
}
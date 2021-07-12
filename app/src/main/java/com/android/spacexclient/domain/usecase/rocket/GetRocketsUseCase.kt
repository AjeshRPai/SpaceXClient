package com.android.spacexclient.domain.usecase.rocket

import com.android.spacexclient.domain.model.RocketModel
import com.android.spacexclient.domain.model.Query
import io.reactivex.Single

interface GetRocketsUseCase {
    operator fun invoke(query: Query): Single<Result<List<RocketModel>>>
}



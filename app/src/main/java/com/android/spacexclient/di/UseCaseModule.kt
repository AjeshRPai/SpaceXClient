package com.android.spacexclient.di

import com.android.spacexclient.data.repository.RocketRepository
import com.android.spacexclient.domain.usecase.rocket.GetRocketsUseCaseImpl
import com.android.spacexclient.domain.usecase.rocket.RefreshRocketsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun getRocketsUseCase(repository: RocketRepository):GetRocketsUseCaseImpl {
        return GetRocketsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun getRefreshRocketsUseCase(repository: RocketRepository): RefreshRocketsUseCaseImpl {
        return RefreshRocketsUseCaseImpl(repository)
    }
}
package com.android.spacexclient.di

import com.android.spacexclient.RocketRepository
import com.android.spacexclient.domain.GetRocketsUseCaseImpl
import com.android.spacexclient.domain.RefreshRocketsUseCaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
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
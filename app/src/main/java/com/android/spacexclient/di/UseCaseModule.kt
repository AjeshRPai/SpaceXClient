package com.android.spacexclient.di

import com.android.spacexclient.RocketRepository
import com.android.spacexclient.domain.GetActiveRocketsUseCase
import com.android.spacexclient.domain.GetRocketsUseCase
import com.android.spacexclient.domain.Mapper
import com.android.spacexclient.domain.RefreshRocketsUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
class UseCaseModule {

    @Provides
    @Singleton
    fun getRocketsUseCase(repository: RocketRepository):GetRocketsUseCase {
        return GetRocketsUseCase(repository)
    }

    @Provides
    @Singleton
    fun getActiveRocketsUseCase(repository: RocketRepository): GetActiveRocketsUseCase {
        return GetActiveRocketsUseCase(repository)
    }

    @Provides
    @Singleton
    fun getRefreshRocketsUseCase(repository: RocketRepository): RefreshRocketsUseCase {
        return RefreshRocketsUseCase(repository)
    }
}
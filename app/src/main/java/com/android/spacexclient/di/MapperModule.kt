package com.android.spacexclient.di

import com.android.spacexclient.data.mapper.RocketDtoMapper
import com.android.spacexclient.domain.mapper.RocketDomainMapper
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

@dagger.Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
class MapperModule {

    @Singleton
    @Provides
    fun getMapper(): RocketDomainMapper = RocketDomainMapper()

    @Singleton
    @Provides
    fun getLocalDtoMapper(): RocketDtoMapper = RocketDtoMapper()


}
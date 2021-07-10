package com.android.spacexclient.di

import com.android.spacexclient.database.RocketDtoMapper
import com.android.spacexclient.domain.RocketDomainMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapperModule {

    @Singleton
    @Provides
    fun getMapper():RocketDomainMapper = RocketDomainMapper()

    @Singleton
    @Provides
    fun getLocalDtoMapper():RocketDtoMapper = RocketDtoMapper()


}
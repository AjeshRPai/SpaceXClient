package com.android.spacexclient.di

import com.android.spacexclient.data.repository.RocketRepository
import com.android.spacexclient.data.api.RocketApi
import com.android.spacexclient.data.database.RocketDao
import com.android.spacexclient.data.mapper.RocketDtoMapper
import com.android.spacexclient.domain.mapper.RocketDomainMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class,RoomModule::class,MapperModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRocketRepo(rocketDao:RocketDao,
                          rocketApi:RocketApi,
                          rocketDomainMapper: RocketDomainMapper,
                          rocketDtoMapper: RocketDtoMapper
    )
       =  RocketRepository(rocketDao,rocketApi,rocketDomainMapper, rocketDtoMapper)


}
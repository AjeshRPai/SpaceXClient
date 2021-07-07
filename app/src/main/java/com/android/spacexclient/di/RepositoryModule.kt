package com.android.spacexclient.di

import com.android.spacexclient.RocketRepository
import com.android.spacexclient.api.RocketApi
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.domain.Mapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class,RoomModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRocketRepo(rocketDao:RocketDao,rocketApi:RocketApi) = RocketRepository(rocketDao,rocketApi,dataMapper = Mapper)

}
package com.android.spacexclient.di

import com.android.spacexclient.presentation.utils.AppSchedulerProvider
import com.android.spacexclient.presentation.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
class SchedulerModule {

    @Provides
    @Singleton
    fun getSchedulers(): SchedulerProvider {
        return AppSchedulerProvider()
    }
}
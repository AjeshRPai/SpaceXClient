package com.android.spacexclient.di

import com.android.spacexclient.presentation.AppSchedulerProvider
import com.android.spacexclient.presentation.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SchedulerModule {

    @Provides
    @Singleton
    fun getSchedulers(): SchedulerProvider {
        return AppSchedulerProvider()
    }
}
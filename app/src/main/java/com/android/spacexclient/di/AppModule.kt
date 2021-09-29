package com.android.spacexclient.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.android.spacexclient.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
class AppModule() {

    @Provides
    @Singleton
    fun providePreferences(application:Application): SharedPreferences {
        val sharedPreferenceFileKey = application.getString(R.string.shared_preference_file_key)
        return application.getSharedPreferences(sharedPreferenceFileKey, Context.MODE_PRIVATE)
    }
}
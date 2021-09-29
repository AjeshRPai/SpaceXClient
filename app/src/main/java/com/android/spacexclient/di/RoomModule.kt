package com.android.spacexclient.di

import android.app.Application
import androidx.room.Room
import com.android.spacexclient.data.database.RocketDao
import com.android.spacexclient.data.database.SpaceXDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton


@Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
class RoomModule {

    private lateinit var database: SpaceXDatabase

    private val databaseName = "spacex_database"

    @Singleton
    @Provides
    fun provideSpaceXDatabase(application: Application): SpaceXDatabase {
         database = Room.databaseBuilder(
            application,
            SpaceXDatabase::class.java, databaseName)
             .fallbackToDestructiveMigration()
            .build()
        return database
    }

    @Singleton
    @Provides
    fun providesRocketDao(database: SpaceXDatabase): RocketDao {
        return database.rocketDao()
    }

}
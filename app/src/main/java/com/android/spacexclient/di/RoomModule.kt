package com.android.spacexclient.di

import android.app.Application
import androidx.room.Room
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.database.RocketDao2
import com.android.spacexclient.database.SpaceXDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RoomModule() {

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

    @Singleton
    @Provides
    fun providesRocketDao2(database: SpaceXDatabase): RocketDao2 {
        return database.rocketDao2()
    }

}
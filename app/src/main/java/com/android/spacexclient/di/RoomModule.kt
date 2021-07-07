package com.android.spacexclient.di

import android.app.Application
import androidx.room.Room
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.database.SpaceXDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application): SpaceXDatabase {
         return Room.databaseBuilder(
            application,
            SpaceXDatabase::class.java,
            "spacex_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesRocketDao(database: SpaceXDatabase): RocketDao {
        return database.rocketDao()
    }

}
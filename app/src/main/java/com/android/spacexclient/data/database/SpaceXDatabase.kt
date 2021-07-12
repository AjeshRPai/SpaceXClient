package com.android.spacexclient.data.database

import androidx.room.*
import com.android.spacexclient.data.database.model.LocalRocketModel

@Database(entities = [LocalRocketModel::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SpaceXDatabase : RoomDatabase() {
    abstract fun rocketDao(): RocketDao
}

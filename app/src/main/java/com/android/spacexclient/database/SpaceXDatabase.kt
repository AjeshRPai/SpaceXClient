package com.android.spacexclient.database

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface RocketDao {

    @Query("SELECT * FROM rockets")
    fun getRockets(): Single<List<LocalRocketModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rockets: List<LocalRocketModel>):List<Long>

    @Query("DELETE FROm rockets")
    fun deleteAll()
}


@Database(entities = [LocalRocketModel::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SpaceXDatabase : RoomDatabase() {
    abstract fun rocketDao(): RocketDao
}

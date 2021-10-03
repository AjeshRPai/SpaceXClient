package com.android.spacexclient.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.spacexclient.data.database.model.LocalRocketModel

@Dao
interface RocketDao {

    @Query("SELECT * FROM rockets")
    suspend fun getRockets(): List<LocalRocketModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rockets: List<LocalRocketModel>):List<Long>

    @Query("DELETE FROm rockets")
    fun deleteAll()
}
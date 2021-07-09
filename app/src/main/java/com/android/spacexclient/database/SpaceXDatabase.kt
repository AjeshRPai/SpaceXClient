package com.android.spacexclient.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Entity(tableName = "rockets")
data class LocalRocketModel(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "active")
    val active: Boolean,
    @ColumnInfo(name = "images")
    val flickrImages: List<String>,
    @ColumnInfo(name = "engines")
    val engines: Int
)


@Dao
interface RocketDao {

    @Query("SELECT * FROM rockets")
    fun getRockets(): Observable<List<LocalRocketModel>>

    @Query("SELECT * FROM rockets where active = 1")
    fun getActiveRockets(): Observable<List<LocalRocketModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rockets: List<LocalRocketModel>): Completable

    @Query("DELETE FROm rockets")
    fun deleteAll(): Completable
}


@Database(entities = [LocalRocketModel::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SpaceXDatabase : RoomDatabase() {
    abstract fun rocketDao(): RocketDao
}

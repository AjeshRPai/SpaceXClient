package com.android.spacexclient.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "engines")
    val engines: Int,
    @ColumnInfo(name = "images")
    val flickrImages: List<String>,
    @ColumnInfo(name = "year")
    val yearOfLaunch: String
)

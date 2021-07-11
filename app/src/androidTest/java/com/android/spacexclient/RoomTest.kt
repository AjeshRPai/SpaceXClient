package com.android.spacexclient

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.spacexclient.database.LocalRocketModel
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.database.SpaceXDatabase
import com.android.spacexclient.domain.RocketModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RoomTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: RocketDao
    private lateinit var db: SpaceXDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, SpaceXDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.rocketDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun shouldNotContainAnyEntries() {
        dao.getRockets()
            .test()
            .assertValue { it.equals(emptyList<RocketModel>()) }
    }


    @Test
    fun writeAndGet() {
        val dbEntries = getRockets()

        dao.insertAll(dbEntries)

        dao.getRockets()
            .test()
            .assertValue {
                print(it)
                it == dbEntries
            }

        dao.deleteAll()
    }


    @Test
    fun shouldNotContainAnyEntriesAfterDeletion() {
        val dbEntries = getRockets()

        // given
        dao.insertAll(dbEntries)

        //when
        dao.deleteAll()

        //then
        dao.getRockets().test()
            .assertValue { it.equals(emptyList<RocketModel>()) }
    }

    private fun getRockets(): List<LocalRocketModel> {
        val images = listOf("Image1", "image2")
        val inActive = LocalRocketModel(id = "1233", "Spacex3", "US", false, 1, images, "2019")
        return listOf(inActive)
    }

}


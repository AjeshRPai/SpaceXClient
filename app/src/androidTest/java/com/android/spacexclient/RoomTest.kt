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
        val dbEntries = getActiveRockets()+getInactiveRockets()

        dao.insertAll(dbEntries)
            .blockingAwait()

        dao.getRockets()
            .test()
            .assertValue {
                print(it)
                it == dbEntries
            }

        dao.deleteAll()
            .blockingAwait()
    }


    @Test
    fun shouldNotContainAnyEntriesAfterDeletion() {
        val dbEntries = getActiveRockets()+getInactiveRockets()

        // given
        dao.insertAll(dbEntries)
            .blockingAwait()

        //when
        dao.deleteAll()
            .blockingAwait()

        //then
        dao.getRockets().test()
            .assertValue { it.equals(emptyList<RocketModel>()) }
    }

    @Test
    fun shouldFetchOnlyActiveRockets() {

        val dbEntries = getActiveRockets()+getInactiveRockets()

        dao.insertAll(dbEntries)
            .blockingAwait()

        dao.getActiveRockets()
            .test()
            .assertValueCount(1)
            .assertValue {
                it == getActiveRockets()
            }

        dao.deleteAll().blockingAwait()

    }

    private fun getInactiveRockets(): List<LocalRocketModel> {
        val images = listOf("Image1", "image2")
        val inActive = LocalRocketModel(id = "1233", "Spacex3", "US", false, images, 1)
        return listOf(inActive)
    }

    private fun getActiveRockets(): List<LocalRocketModel> {
        val images = listOf("Image1", "image2")
        val active = LocalRocketModel(id = "1234", "Spacex", "US", true, images, 1)
        return listOf(active)
    }




}


package com.android.spacexclient.dataLayer

import com.android.spacexclient.RocketRepository
import com.android.spacexclient.api.Engines
import com.android.spacexclient.api.NetworkRocketModel
import com.android.spacexclient.api.RocketApi
import com.android.spacexclient.database.LocalRocketModel
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.database.convertToDbModel
import com.android.spacexclient.domain.Mapper
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class RocketRepositoryTest {

    lateinit var rocketRepository: RocketRepository

    @Mock
    lateinit var rocketDao: RocketDao

    @Mock
    lateinit var rocketApi: RocketApi

    @Mock
    lateinit var mapper: Mapper

    @Before
    fun setUp() {
        rocketRepository = RocketRepository(rocketDao, rocketApi, mapper)
    }

    @Test
    fun `if network is available and insertion is successful then should return movies`() {
        // given
        val engines = Engines(1)
        val images = listOf("Image1Url", "Image2Url")
        val rocket = NetworkRocketModel(false, "US", engines, images, "1234", "Spacex")

        val apiResponse = arrayListOf(rocket)
        val dbEntry = convertToDbModel(apiResponse)
        val domainModels = Mapper.map(dbEntry)

        val expectedResponse = Result.success(domainModels)

        Mockito.`when`(rocketApi.getRockets())
            .thenReturn(Observable.just(apiResponse))
        Mockito.`when`(rocketDao.insertAll(dbEntry))
            .thenReturn(Completable.complete())
        Mockito.`when`(rocketDao.getRockets())
            .thenReturn(Observable.just(dbEntry))
        Mockito.`when`(mapper.map(dbEntry))
            .thenReturn(domainModels)

        print(expectedResponse)
        // when
        rocketRepository
            .getRockets()
            .test()
            .assertNoErrors()
            .assertValue {
                it == expectedResponse
            }
        //then it should return all the movies

    }


    @Test
    fun `should support offline mode - if there are entries in the db then that should be emitted and network error should be ignored`() {

        val images = listOf<String>()
        val model = LocalRocketModel(id = "1234", "Spacex", "US", true, images, 1)

        val dbEntries = arrayListOf(model)
        val domainModels = Mapper.map(dbEntries)

        val networkError = Throwable("No Network")

        Mockito.`when`(rocketApi.getRockets())
            .thenReturn(Observable.error(networkError))
        Mockito.`when`(rocketDao.getRockets())
            .thenReturn(Observable.just(dbEntries))
        Mockito.`when`(mapper.map(dbEntries))
            .thenReturn(domainModels)

        val expectedResponse = Result.success(domainModels)

        // when
        rocketRepository
            .getRockets()
            .test()
            .assertValueCount(1)
            .assertValue {
                it == expectedResponse
            }
            .assertComplete()

    }

    @Test
    fun `should refresh with updated items from network`() {

        val images = listOf<String>()
        val model = LocalRocketModel(id = "1234", "Spacex", "US", true, images, 1)

        val dbEntries = arrayListOf(model)
        val domainModels = Mapper.map(dbEntries)

        // new details
        val engines = Engines(1)
        val images2 = listOf("Image1Url", "Image2Url")
        val rocket1 = NetworkRocketModel(false, "US", engines, images2, "1234", "Spacex")
        val rocket2 = NetworkRocketModel(false, "US", engines, images2, "1233", "Spacex")

        val apiResponse = listOf(rocket1,rocket2)
        val dbEntries2 = convertToDbModel(apiResponse)
        val domainModels2 = mapper.map(dbEntries2)

        Mockito.`when`(rocketDao.getRockets())
            .thenReturn(Observable.empty())

        Mockito.`when`(rocketApi.getRockets())
            .thenReturn(Observable.just(apiResponse).delay(500,TimeUnit.MILLISECONDS))

        Mockito.`when`(rocketDao.insertAll(dbEntries2))
            .thenReturn(Completable.complete())

        Mockito.`when`(rocketDao.getRockets())
            .thenReturn(Observable.just(dbEntries2).delay(500, TimeUnit.MILLISECONDS))

        Mockito.`when`(mapper.map(dbEntries2))
            .thenReturn(domainModels2)

        val expectedResponse = Result.success(domainModels)

        // when
        rocketRepository
            .getRockets()
            .test()
            .assertValue {
                print(it)
                it.isSuccess
            }

    }

}
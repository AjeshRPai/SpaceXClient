/*
package com.android.spacexclient.dataLayer

import com.android.spacexclient.RocketRepository
import com.android.spacexclient.api.Engines
import com.android.spacexclient.api.NetworkRocketModel
import com.android.spacexclient.api.RocketApi
import com.android.spacexclient.database.LocalRocketModel
import com.android.spacexclient.database.RocketDao
import com.android.spacexclient.database.convertToDbModel
import com.android.spacexclient.domain.Mapper
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class RocketRepositoryTest {

    private val rocketDao: RocketDao = mock()

    private val rocketApi: RocketApi = mock()

    private val mapper: Mapper = mock()

    private val rocketRepository = RocketRepository(rocketDao, rocketApi, mapper)

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
            .assertValue{
                it == expectedResponse
            }


    }

    @Test
    fun `should refresh with updated items from network`() {

        val dbEntries = getAlreadyPresentDbEntries()
        val domainModels = Mapper.map(dbEntries)

        // new details
        val apiResponse = getUpdatedApiResponse()

        val dbEntries2 = convertToDbModel(apiResponse)
        val domainModels2 = mapper.map(dbEntries2)

        Mockito.`when`(rocketDao.getRockets())
            .thenReturn(Observable.just(dbEntries))

        Mockito.`when`(mapper.map(dbEntries))
            .thenReturn(domainModels)

        val expectedResponse = Result.success(listOf(domainModels))

        Mockito.`when`(rocketApi.getRockets())
            .thenReturn(Observable.just(apiResponse))

        rocketRepository
            .getRockets()
            .test().assertValue {
                print(it)
                it.equals(expectedResponse)
            }


        Mockito.`when`(rocketDao.getRockets())
            .thenReturn(Observable.just(dbEntries2))

        Mockito.`when`(mapper.map(dbEntries2))
            .thenReturn(domainModels2)


        val expectedResponse2 = Result.success(listOf(domainModels2))

        // when
        rocketRepository
            .getRockets()
            .test()
            .assertValueAt(0)
            {
                print(it)
                it.equals(expectedResponse)
            }.assertValueAt(1)
            {
                print(it)
                it.equals(expectedResponse2)
            }

    }

    private fun getAlreadyPresentDbEntries(): ArrayList<LocalRocketModel> {
        val images = listOf<String>()
        val model = LocalRocketModel(id = "1", "Spacex", "US", true, images, 1)
        val model2 = LocalRocketModel(id = "2", "Spacex", "US", true, images, 1)

        val dbEntries = arrayListOf(model, model2)
        return dbEntries
    }

    private fun getUpdatedApiResponse(): List<NetworkRocketModel> {
        val engines = Engines(1)
        val images2 = listOf("Image1Url", "Image2Url")
        val rocket1 = NetworkRocketModel(false, "US", engines, images2, "1", "Spacex")
        val rocket2 = NetworkRocketModel(false, "US", engines, images2, "2", "Spacex")
        val rocket3 = NetworkRocketModel(false, "US", engines, images2, "3", "Spacex")
        val rocket4 = NetworkRocketModel(false, "US", engines, images2, "4", "Spacex")

        val apiResponse = listOf(rocket1, rocket2, rocket3, rocket4)
        return apiResponse
    }

}*/

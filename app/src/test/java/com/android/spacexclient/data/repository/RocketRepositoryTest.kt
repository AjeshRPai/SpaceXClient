package com.android.spacexclient.data.repository

import com.android.spacexclient.data.api.model.NetworkRocketModel
import com.android.spacexclient.data.api.RocketApi
import com.android.spacexclient.data.database.model.LocalRocketModel
import com.android.spacexclient.data.database.RocketDao
import com.android.spacexclient.data.mapper.RocketDtoMapper
import com.android.spacexclient.domain.mapper.RocketDomainMapper
import com.android.spacexclient.domain.model.RocketModel
import com.android.spacexclient.domain.model.Query
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException


@RunWith(MockitoJUnitRunner::class)
class RocketRepositoryTest {

    private val rocketDao: RocketDao = mock()

    private val rocketApi: RocketApi = mock()

    private val domainMapper: RocketDomainMapper = mock()

    private val dtoMapper: RocketDtoMapper = mock()

    private val sut = RocketRepository(rocketDao, rocketApi, domainMapper, dtoMapper)

//    @Test
//    fun `if network is available then should return api response`() {
//
//        val apiResponse = listOf(getNetworkModel())
//        val expectedResponse = Result.success(listOf(getDomainModel()))
//
//        whenever(rocketApi.getRockets()).thenReturn(Single.just(apiResponse))
//        whenever(dtoMapper.map(getNetworkModel())).thenReturn(getDbModel())
//        whenever(domainMapper.map(getDbModel())).thenReturn(getDomainModel())
//
//        // when
//        sut.getRockets(Query.EMPTY)
//            .test()
//            .assertNoErrors()
//            .assertValue {
//                it == expectedResponse
//            }
//
//        //then it should return all the values from api response
//
//    }
//
//
//    @Test
//    fun `should support offline mode - if there are entries in the db then that should be emitted and network error should be ignored`() {
//
//        // given
//        val apiResponse = IOException("No Network")
//        val expectedResponse = Result.success(listOf(getDomainModel()))
//
//        whenever(rocketApi.getRockets()).thenReturn(Single.error(apiResponse))
//        whenever(rocketDao.getRockets()).thenReturn(Single.just(arrayListOf(getDbModel())))
//        whenever(domainMapper.map(getDbModel())).thenReturn(getDomainModel())
//
//        // when
//        sut.getRockets(Query.EMPTY)
//            .test()
//            .assertValue {
//                it == expectedResponse
//            }
//    }
//
//    @Test
//    fun `should return Network error in case if its not Connectivity Issue`() {
//
//        // given
//        val apiResponse = Exception("Connectivity Issue")
//
//        whenever(rocketApi.getRockets()).thenReturn(Single.error(apiResponse))
//
//        // when
//        sut.getRockets(Query(true))
//            .test()
//            .assertValue {
//                print(it)
//                it.isFailure
//            }
//            .assertValue{
//                it.exceptionOrNull()!!.message ==
//                        apiResponse.message
//            }
//
//    }
//
//    @Test
//    fun `should return No Result in case of No Data with Active Rocket State from Db`() {
//
//        // given
//        val expectedMessage = "No Results"
//        val apiResponse = IOException("Connectivity Issue")
//
//        whenever(rocketApi.getRockets()).thenReturn(Single.error(apiResponse))
//        whenever(rocketDao.getRockets()).thenReturn(Single.just(listOf(getDbModel())))
//
//        // when
//        sut.getRockets(Query(true))
//            .test()
//            .assertValue {
//                it.isFailure
//            }
//            .assertValue{
//                print(it)
//                it.exceptionOrNull()!!.message ==
//                        expectedMessage
//            }
//
//    }
//
//    @Test
//    fun `should return No Result in case of No Data with Active Rocket State from API Response`() {
//
//        // given
//
//        val expectedMessage = "No Results"
//        val apiResponse = listOf(getNetworkModel())
//
//        whenever(rocketApi.getRockets()).thenReturn(Single.just(apiResponse))
//        whenever(dtoMapper.map(getNetworkModel())).thenReturn(getDbModel())
//
//        // when
//        sut.getRockets(Query(true))
//            .test()
//            .assertValue {
//                it.isFailure
//            }
//            .assertValue{
//                it.exceptionOrNull()?.message == expectedMessage
//            }
//
//    }
//
//    @Test
//    fun `should return updated results only when refresh is called`() {
//
//        //given
//        val apiResponse = listOf(getNetworkModel())
//        val expectedResponse = Result.success(listOf(getDomainModel()))
//
//        whenever(rocketApi.getRockets()).thenReturn(Single.just(apiResponse))
//        whenever(dtoMapper.map(getNetworkModel())).thenReturn(getDbModel())
//        whenever(domainMapper.map(getDbModel())).thenReturn(getDomainModel())
//
//        // when
//        sut.refreshRocketList()
//            .test()
//            .assertValue {
//                it == expectedResponse
//            }
//
//    }
//
//    @Test
//    fun `should return error when refresh fails`() {
//
//        //given
//        val apiResponse = IOException("No Network Error")
//
//        whenever(rocketApi.getRockets()).thenReturn(Single.error(apiResponse))
//
//        // when
//        sut.refreshRocketList()
//            .test()
//            .assertValue {
//                it.isFailure
//            }.assertValue {
//                it.exceptionOrNull()?.message == apiResponse.message
//            }
//
//    }


    private fun getDbModel(): LocalRocketModel {
        return LocalRocketModel(
            "1",
            "Space x",
            "US",
            false,
            1,
            listOf("Image1Url", "Image2Url"),
            "2019"
        )
    }

    private fun getNetworkModel(): NetworkRocketModel {
        return NetworkRocketModel(
            "1",
            "Space x",
            "US",
            false,
            NetworkRocketModel.Engines(1),
            listOf("Image1Url", "Image2Url"),
            "2019-06-23"
        )
    }

    private fun getDomainModel(): RocketModel {
        return RocketModel(
            "1",
            "Space x",
            "US",
            false,
            listOf("Image1Url", "Image2Url"),
            1,
            "2019"
        )
    }

}

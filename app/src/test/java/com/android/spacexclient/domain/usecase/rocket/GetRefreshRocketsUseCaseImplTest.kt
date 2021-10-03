package com.android.spacexclient.domain.usecase.rocket


import com.android.spacexclient.data.repository.RocketRepository
import com.android.spacexclient.domain.model.RocketModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test


class GetRefreshRocketsUseCaseImplTest {

    private val repository: RocketRepository = mock();

    private val sut = RefreshRocketsUseCaseImpl(repository)

    @Test
    fun `invoke should return repository result success`() {
        val models:List<RocketModel> = mock()
        val success = Result.success(models)

        whenever(repository.refreshRocketList()).thenReturn(Single.just(success))
        val actual = sut()

        actual.test()
            .assertValue { it.equals(success)}
    }

    @Test
    fun `invoke should return repository result failure`() {
        val throwable:Throwable = mock()
        val failure = Result.failure<List<RocketModel>>(throwable)

        whenever(repository.refreshRocketList()).thenReturn(Single.just(failure))
        val actual = sut()

        actual.test()
            .assertValue { it.equals(failure)}
    }
}
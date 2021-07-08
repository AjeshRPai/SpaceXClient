package com.android.spacexclient.domain


import com.android.spacexclient.RocketRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Observable
import org.junit.Test


class GetActiveRocketsUseCaseImplTest {

    private val repository:RocketRepository = mock();

    private val sut = GetActiveRocketsUseCaseImpl(repository)

    @Test
    fun `invoke should return repository result success`() {
        val models = listOf(RocketModel(id = "1234", "Spacex", "US", true, listOf("Image1", "image2"), 1))
        val success = Result.success(models)

        whenever(repository.getActiveRockets()).thenReturn(Observable.just(success))
        val actual = sut()

        actual.test()
            .assertValue { it.equals(success)}
    }

    @Test
    fun `invoke should return repository result failure`() {
        val throwable = Throwable("Some error")
        val failure = Result.failure<List<RocketModel>>(throwable)

        whenever(repository.getActiveRockets()).thenReturn(Observable.just(failure))
        val actual = sut()

        actual.test()
            .assertValue { it.equals(failure)}
    }
}
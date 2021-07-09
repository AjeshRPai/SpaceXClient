package com.android.spacexclient.domain


import com.android.spacexclient.RocketRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Observable
import org.junit.Test


class GetRocketsUseCaseImplTest {

    private val repository:RocketRepository = mock();

    private val sut = GetRocketsUseCaseImpl(repository)

    @Test
    fun `invoke should return repository result success`() {
        val images = listOf("Image1", "image2")
        val model = RocketModel(id = "1234", "Spacex", "US", true, images, 1)
        val models = listOf(model)

        val success = Result.success(models)

        whenever(repository.getRockets()).thenReturn(Observable.just(success))
        val actual = sut()

        actual.test()
            .assertValue { it.equals(success)}
    }

    @Test
    fun `invoke should return repository result failure`() {
        val throwable = Throwable("Some error")
        val failure = Result.failure<List<RocketModel>>(throwable)

        whenever(repository.getRockets()).thenReturn(Observable.just(failure))
        val actual = sut()

        actual.test()
            .assertValue { it.equals(failure)}
    }
}
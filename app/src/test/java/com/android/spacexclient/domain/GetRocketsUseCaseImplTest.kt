package com.android.spacexclient.domain


import com.android.spacexclient.RocketRepository
import com.android.spacexclient.presentation.utils.Query
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test


class GetRocketsUseCaseImplTest {

    private val repository: RocketRepository = mock();

    private val sut = GetRocketsUseCaseImpl(repository)

    private val query: Query = mock()

    @Test
    fun `invoke should return repository result success`() {
        val models:List<RocketModel> = mock()
        val success = Result.success(models)

        whenever(repository.getRockets(query)).thenReturn(Single.just(success))
        val actual = sut(query)

        actual.test()
            .assertValue { it.equals(success) }
    }

    @Test
    fun `invoke should return repository result failure`() {
        val throwable:Throwable = mock()
        val failure = Result.failure<List<RocketModel>>(throwable)

        whenever(repository.getRockets(query)).thenReturn(Single.just(failure))
        val actual = sut(query)

        actual.test()
            .assertValue { it.equals(failure) }
    }
}
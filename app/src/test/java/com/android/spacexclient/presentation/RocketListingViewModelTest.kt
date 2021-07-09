package com.android.spacexclient.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.spacexclient.domain.GetActiveRocketsUseCaseImpl
import com.android.spacexclient.domain.GetRocketsUseCaseImpl
import com.android.spacexclient.domain.RefreshRocketsUseCaseImpl
import com.android.spacexclient.domain.RocketModel
import com.jraska.livedata.TestObserver
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RocketListingViewModelTest {

    @get:Rule
    val instantTaskTestRule: TestRule = InstantTaskExecutorRule()

    private val getRocketsUseCase = mock<GetRocketsUseCaseImpl>()
    private val getActiveRocketsUseCaseImpl: GetActiveRocketsUseCaseImpl = mock()
    private val refreshRocketsUseCaseImpl: RefreshRocketsUseCaseImpl = mock()

    private val testSchedulerProvider = TestSchedulerProvider(TestScheduler())

    private val sut = RocketListingViewModel(getRocketsUseCase,getActiveRocketsUseCaseImpl, refreshRocketsUseCaseImpl, testSchedulerProvider)

    private lateinit var stateObserver: TestObserver<UIState<List<RocketModel>>>

    @Before
    fun setUp() {
        stateObserver = TestObserver.test(sut.getAllRockets())
    }

    @Test
    fun testListOfRockets() {
        whenever(getRocketsUseCase()).thenReturn(Observable.empty())

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        stateObserver.assertValue(UIState.Loading)
    }

    @Test
    fun `loadTransactions should show content state when transactions are retrieved`() {
        val models = listOf(RocketModel(id = "1234", "Spacex", "US", true, listOf("Image1", "image2"), 1))
        whenever(getRocketsUseCase()).thenReturn(Observable.just(Result.success(models)))

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        stateObserver.assertValueHistory(UIState.Loading, UIState.Success(models))
    }

    @Test
    fun `loadTransactions should show error state when transactions cannot be retrieved`() {
        val error_message = "Some Error"
        val failure = Result.failure<List<RocketModel>>(Throwable(error_message))

        whenever(getRocketsUseCase()).thenReturn(Observable.just(failure))

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        stateObserver.assertValueHistory(UIState.Loading, UIState.Error(error_message))
    }

}
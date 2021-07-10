package com.android.spacexclient.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.spacexclient.domain.*
import com.android.spacexclient.presentation.utils.Query
import com.android.spacexclient.presentation.utils.UIState
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

    private val getRocketsUseCaseImpl:GetRocketsUseCaseImpl = mock()
    private val searchRocketsUseCaseImpl: SearchRocketsUseCaseImpl = mock()
    private val refreshRocketsUseCaseImpl: RefreshRocketsUseCaseImpl = mock()

    private val testSchedulerProvider = TestSchedulerProvider(TestScheduler())

    private val sut = RocketListingViewModel(getRocketsUseCaseImpl,searchRocketsUseCaseImpl, refreshRocketsUseCaseImpl, testSchedulerProvider)

    private lateinit var listObserver: TestObserver<UIState<List<RocketModel>>>

    private lateinit var refreshObserver: TestObserver<UIState<List<RocketModel>>?>

    private val query = Query()

    @Before
    fun setUp() {
        listObserver = TestObserver.test(sut.getAllRockets())
        refreshObserver = TestObserver.test(sut.getRefreshing())

    }

    @Test
    fun `when rockets are not fetched it should remain in the loading state`() {
        whenever(getRocketsUseCaseImpl()).thenReturn(Observable.empty())

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        listObserver.assertValue(UIState.Loading)
    }

    @Test
    fun `load rockets should show content state when rockets are retrieved`() {
        val models:List<RocketModel> = mock()
        whenever(getRocketsUseCaseImpl()).thenReturn(Observable.just(Result.success(models)))

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        listObserver.assertValueHistory(UIState.Loading, UIState.Success(models))
    }

    @Test
    fun `load rockets should show error state when rockets cannot be retrieved`() {
        val error_message = "Some Error"
        val failure = Result.failure<List<RocketModel>>(Throwable(error_message))

        whenever(getRocketsUseCaseImpl()).thenReturn(Observable.just(failure))

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        listObserver.assertValueHistory(UIState.Loading, UIState.Error(error_message))
    }

    @Test
    fun `refresh rockets should show error state when rockets cannot be refreshed`() {
        val error_message = "Some Error"
        val failure = Result.failure<List<RocketModel>>(Throwable(error_message))

        whenever(refreshRocketsUseCaseImpl()).thenReturn(Observable.just(failure))

        sut.refreshRocketList()
        testSchedulerProvider.triggerActions()

        refreshObserver.assertValueHistory(UIState.Loading, UIState.Error(error_message))
    }

    @Test
    fun `refresh rockets should remain in loading state while fetching`() {

        whenever(refreshRocketsUseCaseImpl()).thenReturn(Observable.empty())

        sut.refreshRocketList()
        testSchedulerProvider.triggerActions()

        refreshObserver.assertValueHistory(UIState.Loading)
    }



}
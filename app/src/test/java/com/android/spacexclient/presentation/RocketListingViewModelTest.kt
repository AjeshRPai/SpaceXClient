package com.android.spacexclient.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.spacexclient.domain.GetRocketsUseCaseImpl
import com.android.spacexclient.domain.RefreshRocketsUseCaseImpl
import com.android.spacexclient.domain.RocketModel
import com.android.spacexclient.presentation.utils.Query
import com.android.spacexclient.presentation.utils.UIState
import com.jraska.livedata.TestObserver
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Rule
import org.junit.Test


class RocketListingViewModelTest {

    @get:Rule
    val instantTaskTestRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val getRocketsUseCaseImpl:GetRocketsUseCaseImpl = mock()

    private val refreshRocketsUseCaseImpl: RefreshRocketsUseCaseImpl = mock()

    private val testSchedulerProvider = TestSchedulerProvider(TestScheduler())

    private val sut:RocketListingViewModel by lazy {
        RocketListingViewModel(getRocketsUseCaseImpl, refreshRocketsUseCaseImpl, testSchedulerProvider)
    }

    private lateinit var listObserver: TestObserver<UIState<List<RocketModel>>>

    private lateinit var refreshObserver: TestObserver<UIState<List<RocketModel>>?>

    private val query = Query()


    @Test
    fun `when rockets are not fetched it should remain in the loading state`() {
        whenever(getRocketsUseCaseImpl(query)).thenReturn(Single.never())

        listObserver = TestObserver.test(sut.getAllRockets())

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        listObserver.assertValue(UIState.Loading)
    }

    @Test
    fun `load rockets should show content state when rockets are retrieved`() {

        val models:List<RocketModel> = mock()
        whenever(getRocketsUseCaseImpl(query)).thenReturn(Single.just(Result.success(models)))

        listObserver = TestObserver.test(sut.getAllRockets())

        testSchedulerProvider.triggerActions()

        listObserver.assertValueHistory(UIState.Loading, UIState.Success(models))
    }

    @Test
    fun `load rockets should show error state when rockets cannot be retrieved`() {
        val error_message = "Some Error"
        val failure = Result.failure<List<RocketModel>>(Throwable(error_message))

        whenever(getRocketsUseCaseImpl(query)).thenReturn(Single.just(failure))

        listObserver = TestObserver.test(sut.getAllRockets())

        testSchedulerProvider.triggerActions()

        listObserver.assertValueHistory(UIState.Loading,UIState.Error(error_message))
    }

    @Test
    fun `refresh rockets should show error state when rockets cannot be refreshed`() {
        val error_message = "Some Error"
        val failure = Result.failure<List<RocketModel>>(Throwable(error_message))

        whenever(getRocketsUseCaseImpl(query)).thenReturn(Single.never())
        whenever(refreshRocketsUseCaseImpl()).thenReturn(Single.just(failure))

        refreshObserver = TestObserver.test(sut.getRefreshing())

        sut.refreshRocketList()
        testSchedulerProvider.triggerActions()

        refreshObserver.assertValueHistory(UIState.Loading, UIState.Error(error_message))
    }

    @Test
    fun `refresh rockets should remain in loading state while fetching`() {
        whenever(getRocketsUseCaseImpl(query)).thenReturn(Single.never())
        whenever(refreshRocketsUseCaseImpl()).thenReturn(Single.never())

        refreshObserver = TestObserver.test(sut.getRefreshing())

        sut.refreshRocketList()
        testSchedulerProvider.triggerActions()

        refreshObserver.assertValueHistory(UIState.Loading)
    }

    @Test
    fun `rocket state should get content in success state while refreshing`() {
        val models:List<RocketModel> = mock()

        whenever(refreshRocketsUseCaseImpl()).thenReturn(Single.just(Result.success(models)))
        whenever(getRocketsUseCaseImpl(query)).thenReturn(Single.never())

        listObserver = TestObserver.test(sut.getAllRockets())
        refreshObserver = TestObserver.test(sut.getRefreshing())

        sut.refreshRocketList()
        testSchedulerProvider.triggerActions()

        refreshObserver.assertValueHistory(UIState.Loading,UIState.Success(listOf()))

        listObserver.assertValue(UIState.Success(models))
    }


}
package com.android.spacexclient.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.spacexclient.domain.*
import com.android.spacexclient.presentation.utils.Query
import com.android.spacexclient.presentation.utils.UIState
import com.jraska.livedata.TestObserver
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RocketListingViewModelTest {

    @get:Rule
    val instantTaskTestRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val getRocketsUseCaseImpl:GetRocketsUseCaseImpl = mock()

    private val refreshRocketsUseCaseImpl: RefreshRocketsUseCaseImpl = mock()

    private val testSchedulerProvider = TestSchedulerProvider(TestScheduler())

    private val sut = RocketListingViewModel(getRocketsUseCaseImpl, refreshRocketsUseCaseImpl, testSchedulerProvider)

    private lateinit var listObserver: TestObserver<UIState<List<RocketModel>>>

    private lateinit var refreshObserver: TestObserver<UIState<List<RocketModel>>?>

    private val query = Query()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        listObserver = TestObserver.test(sut.getAllRockets())
        refreshObserver = TestObserver.test(sut.getRefreshing())

    }

    //https://stackoverflow.com/questions/52762418/livedata-unit-testing-error-when-using-postvalue-in-init-block

    @Test
    fun `when rockets are not fetched it should remain in the loading state`() {
        whenever(getRocketsUseCaseImpl(query)).thenReturn(Single.never())

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        listObserver.assertValue(UIState.Loading)
    }

    @Test
    fun `load rockets should show content state when rockets are retrieved`() {
        val models:List<RocketModel> = mock()
        whenever(getRocketsUseCaseImpl(query)).thenReturn(Single.just(Result.success(models)))

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        listObserver.assertValueHistory(UIState.Loading, UIState.Success(models))
    }

    @Test
    fun `load rockets should show error state when rockets cannot be retrieved`() {
        val error_message = "Some Error"
        val failure = Result.failure<List<RocketModel>>(Throwable(error_message))

        whenever(getRocketsUseCaseImpl(query)).thenReturn(Single.just(failure))

        sut.getRockets()
        testSchedulerProvider.triggerActions()

        listObserver.assertValueHistory(UIState.Loading, UIState.Error(error_message))
    }

    @Test
    fun `refresh rockets should show error state when rockets cannot be refreshed`() {
        val error_message = "Some Error"
        val failure = Result.failure<List<RocketModel>>(Throwable(error_message))

        whenever(refreshRocketsUseCaseImpl()).thenReturn(Single.just(failure))

        sut.refreshRocketList()
        testSchedulerProvider.triggerActions()

        refreshObserver.assertValueHistory(UIState.Loading, UIState.Error(error_message))
    }

    @Test
    fun `refresh rockets should remain in loading state while fetching`() {

        whenever(refreshRocketsUseCaseImpl()).thenReturn(Single.never())

        sut.refreshRocketList()
        testSchedulerProvider.triggerActions()

        refreshObserver.assertValueHistory(UIState.Loading)
    }

    @Test
    fun `rocket state should get content in success state while refreshing`() {
        val models:List<RocketModel> = mock()

        whenever(refreshRocketsUseCaseImpl()).thenReturn(Single.just(Result.success(models)))

        sut.refreshRocketList()
        testSchedulerProvider.triggerActions()

        refreshObserver.assertValueHistory(UIState.Loading,UIState.Success(listOf()))

        listObserver.assertValue(UIState.Success(models))
    }



}
package com.android.spacexclient.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.spacexclient.domain.GetRocketsUseCaseImpl
import com.android.spacexclient.domain.RefreshRocketsUseCaseImpl
import com.android.spacexclient.domain.RocketModel
import com.android.spacexclient.presentation.utils.Query
import com.android.spacexclient.presentation.utils.SchedulerProvider
import com.android.spacexclient.presentation.utils.UIState
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class RocketListingViewModel @Inject constructor(
    val getRocketsUseCaseImpl: GetRocketsUseCaseImpl,
    val refreshRocketsUseCaseImpl: RefreshRocketsUseCaseImpl,
    val schedulers: SchedulerProvider
) : ViewModel() {

    private val listOfRockets = MutableLiveData<UIState<List<RocketModel>>>()

    private val refreshRockets = MutableLiveData<UIState<List<RocketModel>>?>()

    fun getAllRockets(): MutableLiveData<UIState<List<RocketModel>>> {
        return listOfRockets
    }

    fun getRefreshing(): MutableLiveData<UIState<List<RocketModel>>?> {
        return refreshRockets
    }

    private val compositeDisposable = CompositeDisposable()

    init {
        getRockets()
    }

    fun getRockets(query: Query = Query.EMPTY) {
        Timber.i("Get rockets called with $query")
        listOfRockets.postValue(UIState.Loading)
        val disposable = getRocketsUseCaseImpl(query)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                { handleSuccess(it) },
                { handleFailure(it) }
            )
        compositeDisposable.add(disposable)
    }

    private fun handleSuccess(it: Result<List<RocketModel>>) {
        Timber.e("handle success $it")
        if (it.isSuccess)
            listOfRockets.postValue(UIState.Success(it.getOrDefault(listOf())))
        else
            listOfRockets.postValue(UIState.Error(it.exceptionOrNull()?.message?:""))
    }

    private fun handleFailure(it: Throwable) {
        Timber.i("handle Failure Called $it")
        listOfRockets.postValue(UIState.Error(it.message!!))
    }

    fun refreshRocketList() {
        Timber.i("Refresh Rocket List Called")
        refreshRockets.postValue(UIState.Loading)
        val disposable = refreshRocketsUseCaseImpl()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(
                { handleRefreshSuccess(it) },
                { handleRefreshFailure(it) }
            )
        compositeDisposable.add(disposable)
    }

    private fun handleRefreshFailure(it: Throwable?) {
        listOfRockets.postValue(UIState.Error(it?.message?:"Something went wrong"))
    }

    private fun handleRefreshSuccess(it: Result<List<RocketModel>>) {
        if (it.isSuccess)
        {
            refreshRockets.postValue(UIState.Success(listOf()))
            listOfRockets.postValue(UIState.Success(it.getOrDefault(listOf())))
        }
        else
            refreshRockets.postValue(UIState.Error(it.exceptionOrNull()?.message?:"Something went wrong"))
    }

    fun clearRefreshState() {
        refreshRockets.postValue(null)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}


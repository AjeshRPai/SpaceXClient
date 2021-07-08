package com.android.spacexclient.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.spacexclient.domain.GetActiveRocketsUseCaseImpl
import com.android.spacexclient.domain.GetRocketsUseCaseImpl
import com.android.spacexclient.domain.RefreshRocketsUseCaseImpl
import com.android.spacexclient.domain.RocketModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class RocketListingViewModel @Inject constructor(
    val getRocketsUseCase: GetRocketsUseCaseImpl,
    val getActiveRocketsUseCaseImpl: GetActiveRocketsUseCaseImpl,
    val refreshRocketsUseCaseImpl: RefreshRocketsUseCaseImpl,
) : ViewModel() {

    private val listOfRockets = MutableLiveData<UIState<List<RocketModel>>>()

    private val refreshRockets = MutableLiveData<UIState<List<RocketModel>>?>()

    fun getAllRockets(): LiveData<UIState<List<RocketModel>>> {
        return listOfRockets
    }

    fun getRefreshing(): MutableLiveData<UIState<List<RocketModel>>?> {
        return refreshRockets
    }

    val compositeDisposable = CompositeDisposable()

    fun getRockets() {
        listOfRockets.postValue(UIState.Loading())
        val disposable = getRocketsUseCase
            .invoke()
            .applySchedulers()
            .subscribe(
                { handleSuccess(it) },
                { handleFailure(it) }
            )
        compositeDisposable.add(disposable)
    }

    private fun handleSuccess(it: Result<List<RocketModel>>) {
        if (it.isSuccess)
            listOfRockets.postValue(UIState.Success(it.getOrDefault(listOf())))
        else
            listOfRockets.postValue(UIState.Error(it.toString()))
    }

    private fun handleFailure(it: Throwable) {
        listOfRockets.postValue(UIState.Error(it.message!!))
    }

    fun getActiveRockets() {
        listOfRockets.postValue(UIState.Loading())
        val disposable = getActiveRocketsUseCaseImpl
            .invoke()
            .applySchedulers()
            .subscribe(
                { handleSuccess(it) },
                { handleFailure(it) }
                )
        compositeDisposable.add(disposable)
    }

    fun refreshRocketList() {
        refreshRockets.postValue(UIState.Loading())
        val disposable = refreshRocketsUseCaseImpl
            .invoke()
            .applySchedulers()
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
        Timber.i("handleRefresh Success $it")
        if (it.isSuccess)
        {
            refreshRockets.postValue(UIState.Success(listOf()))
            listOfRockets.postValue(UIState.Success(it.getOrDefault(listOf())))
        }
        else
            refreshRockets.postValue(UIState.Error(it.toString()))
    }

    fun clearRefreshState() {
        refreshRockets.postValue(null)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}


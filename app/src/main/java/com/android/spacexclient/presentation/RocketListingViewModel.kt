package com.android.spacexclient.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.spacexclient.domain.GetActiveRocketsUseCase
import com.android.spacexclient.domain.GetRocketsUseCase
import com.android.spacexclient.domain.RefreshRocketsUseCase
import com.android.spacexclient.domain.RocketModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class RocketListingViewModel @Inject constructor(
    val getRocketsUseCase: GetRocketsUseCase,
    val getActiveRocketsUseCase: GetActiveRocketsUseCase,
    val refreshRocketsUseCase: RefreshRocketsUseCase,
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
            .getRockets()
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
        val disposable = getActiveRocketsUseCase
            .getActiveRockets()
            .applySchedulers()
            .subscribe(
                { handleSuccess(it) },
                { handleFailure(it) }
                )
        compositeDisposable.add(disposable)
    }

    fun refreshRocketList() {
        refreshRockets.postValue(UIState.Loading())
        val disposable = refreshRocketsUseCase
            .refreshRocketEntries()
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
        if (it.isSuccess)
        {
            clearRefreshState()
            listOfRockets.postValue(UIState.Success(it.getOrDefault(listOf())))
        }
        else
            refreshRockets.postValue(UIState.Error(it.toString()))
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun clearRefreshState() {
        refreshRockets.postValue(null)
    }

}





fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applySchedulers(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
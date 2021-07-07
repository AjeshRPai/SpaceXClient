package com.android.spacexclient.presentation

sealed class UIState<out R> {
    data class Success<R>(val list: R) : UIState<R>()
    data class Error(val message: String) : UIState<Nothing>()
    data class Loading(val message: String? = "Fetching Items") : UIState<Nothing>()
}
package com.android.spacexclient.presentation.utils

sealed class UIState<out R> {
    data class Success<R>(val data: R) : UIState<R>()
    data class Error(val message: String) : UIState<Nothing>()
    object Loading : UIState<Nothing>()
}
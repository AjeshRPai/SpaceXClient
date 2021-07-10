package com.android.spacexclient.presentation.utils

data class Query(var onlyActive:Boolean = false)
{
    companion object  {
        val EMPTY = Query()
    }
}
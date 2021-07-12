package com.android.spacexclient.domain.model

data class Query(var onlyActive:Boolean = false)
{
    companion object  {
        val EMPTY = Query()
    }
}
package com.android.spacexclient

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class SpaceXClientApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
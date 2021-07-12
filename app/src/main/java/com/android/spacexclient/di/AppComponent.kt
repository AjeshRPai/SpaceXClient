package com.android.spacexclient.di

import android.app.Application
import android.content.SharedPreferences
import com.android.spacexclient.presentation.view.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AppModule::class,UseCaseModule::class,SchedulerModule::class,ViewModelModule::class]
)
interface AppComponent
{
    fun inject(activity: MainActivity)

    fun getSharedPrefs(): SharedPreferences

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
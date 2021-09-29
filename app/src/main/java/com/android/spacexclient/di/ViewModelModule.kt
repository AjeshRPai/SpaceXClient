package com.android.spacexclient.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.spacexclient.presentation.viewmodel.RocketListingViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
@dagger.hilt.InstallIn(dagger.hilt.android.components.ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RocketListingViewModel::class)
    internal abstract fun postListViewModel(viewModel: RocketListingViewModel): ViewModel

}
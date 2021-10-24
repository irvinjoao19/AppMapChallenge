package gongora.irvin.appmapchallenge.data.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gongora.irvin.appmapchallenge.data.viewModel.MapViewModel
import gongora.irvin.appmapchallenge.data.viewModel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    internal abstract fun bindMapViewModel(mapViewModel: MapViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
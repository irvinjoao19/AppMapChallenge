package gongora.irvin.appmapchallenge.data.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import gongora.irvin.appmapchallenge.ui.MapsActivity

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MapsActivity
}
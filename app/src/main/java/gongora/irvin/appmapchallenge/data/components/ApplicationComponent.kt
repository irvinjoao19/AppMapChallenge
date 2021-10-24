package gongora.irvin.appmapchallenge.data.components

import android.app.Application
import gongora.irvin.appmapchallenge.data.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import gongora.irvin.appmapchallenge.data.module.ActivityBindingModule
import gongora.irvin.appmapchallenge.data.module.RetrofitModule
import gongora.irvin.appmapchallenge.data.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        RetrofitModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}
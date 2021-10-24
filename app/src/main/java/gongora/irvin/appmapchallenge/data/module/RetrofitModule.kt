package gongora.irvin.appmapchallenge.data.module

import dagger.Module
import dagger.Provides
import gongora.irvin.appmapchallenge.data.local.repository.ApiService
import gongora.irvin.appmapchallenge.data.local.repository.AppRepoImp
import gongora.irvin.appmapchallenge.data.local.repository.AppRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RetrofitModule {

    @Provides
    internal fun providesRetrofit(
        gsonFactory: GsonConverterFactory,
        rxJava: RxJava3CallAdapterFactory,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(rxJava)
            .addConverterFactory(gsonFactory)
            .client(client)
            .build()
    }

    @Provides
    internal fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    internal fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    internal fun providesRxJavaCallAdapterFactory(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.create()
    }

    @Provides
    internal fun provideService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

     @Provides
     internal fun provideRepository(apiService: ApiService): AppRepository {
         return AppRepoImp(apiService)
     }

    companion object {
        private const val BASE_URL = "http://api.weatherunlocked.com/api/current/"
    }
}
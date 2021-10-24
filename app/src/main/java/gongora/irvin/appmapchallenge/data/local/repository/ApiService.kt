package gongora.irvin.appmapchallenge.data.local.repository

import gongora.irvin.appmapchallenge.data.local.model.Weather
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface ApiService {

    @Headers("Cache-Control: no-cache")
    @GET("{location}")
    fun getWeather(
        @Path("location") location: String,
        @Query("app_id") app_id: String,
        @Query("app_key") app_key: String,
        @Query("lang") lang: String,
    ): Observable<Weather>
}
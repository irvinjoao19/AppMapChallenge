package gongora.irvin.appmapchallenge.data.local.repository

import gongora.irvin.appmapchallenge.data.local.model.Weather
import io.reactivex.rxjava3.core.Observable

interface AppRepository {

    fun getWeather(location: String, id: String, key: String, lang: String): Observable<Weather>

}
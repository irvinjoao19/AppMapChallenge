package gongora.irvin.appmapchallenge.data.local.repository

import gongora.irvin.appmapchallenge.data.local.model.Weather
import io.reactivex.rxjava3.core.Observable

class AppRepoImp(private val apiService: ApiService) :
//, private val dataBase: AppDataBase) :
    AppRepository {

    override fun getWeather(
        location: String,
        id: String,
        key: String,
        lang: String
    ): Observable<Weather> {
        return apiService.getWeather(location, id, key, lang)
    }
}
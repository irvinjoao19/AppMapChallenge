package gongora.irvin.appmapchallenge.data.viewModel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gongora.irvin.appmapchallenge.data.local.repository.AppRepository
import gongora.irvin.appmapchallenge.data.local.model.Weather
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

import javax.inject.Inject
import java.io.IOException
import java.lang.RuntimeException

class MapViewModel @Inject
internal constructor(private val roomRepository: AppRepository) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val address = MutableLiveData<Address>()
    val weather = MutableLiveData<Weather>()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun searchLocation(search: String, context: Context) {
        val geocoder = Geocoder(context)
        val subject = BehaviorSubject.create<Geocoder>()
        subject.onNext(geocoder)
        subject.hide()
            .observeOn(Schedulers.io())
            .map {
                try {
                    return@map it.getFromLocationName(
                        search, 1
                    )
                } catch (e: IOException) {
                    mensajeError.postValue(e.toString())
                    throw RuntimeException(e)
                }
            }
            .flatMap {
                if (it.size != 0) {
                    return@flatMap Observable.fromIterable(it)
                }
                return@flatMap null
            }

            //.filter { address -> address.maxAddressLineIndex >= 1 }
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Address> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: Address) {
                    address.postValue(t)
                    getWeather("${t.latitude},${t.longitude}",search)
                }

                override fun onError(e: Throwable) {
                    mensajeError.postValue("No se encontro ningún resultado con la palabra: \n$search")
                }
            })
    }

    private fun getWeather(location: String,search:String) {
        roomRepository.getWeather(location = location, id = app_id, key = app_key, lang = lang)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Weather> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: Weather) {
                    weather.postValue(t)
                }

                override fun onError(e: Throwable) {
                    mensajeError.postValue("No hay ningún resultado del clima en $search")
                }
            })
    }

    companion object {
        private const val app_id = "16cf36db"
        private const val app_key = "f49f8f228f0c9d1a52265ae1c41f6ed0"
        private const val lang = "es"
    }

}


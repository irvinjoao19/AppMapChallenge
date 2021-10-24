package gongora.irvin.appmapchallenge.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.KeyEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import gongora.irvin.appmapchallenge.R
import gongora.irvin.appmapchallenge.data.viewModel.MapViewModel
import gongora.irvin.appmapchallenge.data.viewModel.ViewModelFactory
import gongora.irvin.appmapchallenge.helper.Util
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*
import javax.inject.Inject

class MapsActivity : DaggerAppCompatActivity(), OnMapReadyCallback, View.OnClickListener,
    TextView.OnEditorActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mapViewModel: MapViewModel
    private var mMap: GoogleMap? = null
    private val context = this@MapsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_maps)
        bindUI()
    }

    private fun bindUI() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        imgArrowBack.setOnClickListener(this)
        imgDelete.setOnClickListener(this)
        imgMicroSearch.setOnClickListener(this)
        textViewSearch.setOnClickListener(this)
        editTextSearch.setOnEditorActionListener(this)

        mapViewModel =
            ViewModelProvider(this, viewModelFactory).get(MapViewModel::class.java)

        mapViewModel.mensajeError.observe(this, {
            hideLoad()
            Util.snackBarMensaje(window.decorView, it)
        })
        mapViewModel.address.observe(this, {
            hideLoad()
            val camera = CameraPosition.Builder()
                .target(LatLng(it.latitude, it.longitude))
                .zoom(DEFAULT_ZOOM)
                .build()
            mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(camera))
        })
        mapViewModel.weather.observe(this, {
            showCardWeather()
            if (it.wxIcon != "N/A") {
                imgIcon.visibility = View.VISIBLE
                val iconPng = "${it.wxIcon.substring(0, it.wxIcon.length - 4)}.png"
                Picasso.get().load("${Util.URL_IMG}/2/$iconPng").into(imgIcon)
            } else {
                imgIcon.visibility = View.INVISIBLE
            }
            textViewTemp.text = String.format("%s°", it.tempC)
            textViewDescripcion.text = it.wxDesc
            textViewHumidity.text = String.format("%s %s humedad", it.humidPct, "%")
            textViewWind.text = String.format("%s km/h", it.windspdKmh)
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val camera = CameraPosition.Builder()
            .target(DEFAULT_LOCATION)
            .zoom(DEFAULT_ZOOM)
            .build()
        mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(camera))
    }

    private fun microPhone() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.title_micro)
        try {
            microResul1tLauncher.launch(intent)
        } catch (a: ActivityNotFoundException) {
            mapViewModel.setError("Dispositivo no compatible")
        }
    }

    private val microResul1tLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            showText()
            if (result.resultCode == RESULT_OK) {
                val list: ArrayList<String>? =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val y = list?.get(0)!!
                textViewSearch.text = y

                Util.hideKeyboard(context)
                showLoad()
                mapViewModel.searchLocation(y, this)
            }
        }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgMicroSearch -> {
                Util.hideKeyboardFrom(context, v)
                microPhone()
            }
            R.id.imgDelete -> showText()
            R.id.textViewSearch -> {
                Util.showKeyboard(editTextSearch, context)
                showEdiText()
            }
            R.id.imgArrowBack -> {
                Util.hideKeyboardFrom(context, v)
                showText()
                textViewSearch.text = getString(R.string.search_here)
            }
        }
    }

    override fun onEditorAction(v: TextView, p1: Int, p2: KeyEvent?): Boolean {
        if (v.text.toString().isNotEmpty()) {
            showLoad()
            mapViewModel.searchLocation(v.text.toString(), context)
            Util.hideKeyboard(context)
        }
        return false
    }

    private fun showLoad() {
        progressBar.visibility = View.VISIBLE
        imgMicroSearch.visibility = View.GONE
    }

    private fun hideLoad() {
        cardWeather.visibility = View.GONE
        progressBar.visibility = View.GONE
        imgDelete.visibility = View.VISIBLE
        imgMicroSearch.visibility = View.VISIBLE
    }

    private fun showEdiText() {
        textViewSearch.visibility = View.GONE
        editTextSearch.visibility = View.VISIBLE
        editTextSearch.requestFocus()
    }

    private fun showText() {
        textViewSearch.text = getString(R.string.search_here)
        editTextSearch.text = null
        editTextSearch.visibility = View.GONE
        textViewSearch.visibility = View.VISIBLE
        imgDelete.visibility = View.GONE
        cardWeather.visibility = View.GONE
    }

    private fun showCardWeather() {
        cardWeather.apply {
            alpha = 0f
            visibility = View.GONE
            animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(null)
        }
        cardWeather.animate()
            .alpha(1f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    cardWeather.visibility = View.VISIBLE
                    Util.animate(
                        cardWeather,
                        View.ALPHA,
                        0.0f,
                        1.0f,
                        500,
                        LinearInterpolator()
                    )
                    Util.animate(
                        cardWeather,
                        View.TRANSLATION_Y,
                        cardWeather.translationX,
                        cardWeather.translationX - 100f,
                        500,
                        DecelerateInterpolator()
                    )
                }
            })
    }

    companion object {
        private const val DEFAULT_ZOOM = 12f
        private val DEFAULT_LOCATION = LatLng(-12.036175, -76.999561)
    }
}

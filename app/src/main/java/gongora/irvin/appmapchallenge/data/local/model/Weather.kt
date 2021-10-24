package gongora.irvin.appmapchallenge.data.local.model

import com.google.gson.annotations.SerializedName

open class Weather {
    var lat: Double = 0.0
    var lon: Double = 0.0

    @SerializedName("alt_m")
    var altM: Int = 0

    @SerializedName("alt_ft")
    var altFt: Double =0.0

    @SerializedName("wx_desc")
    var wxDesc: String = ""

    @SerializedName("wx_code")
    var wxCode: Int = 0

    @SerializedName("wx_icon")
    var wxIcon: String = ""

    @SerializedName("temp_c")
    var tempC: Int =0

    @SerializedName("temp_f")
    var tempF: Double = 0.0

    @SerializedName("feelslike_c")
    var feelslikeC: Double = 0.0

    @SerializedName("feelslike_f")
    var feelslikeF: Double = 0.0

    @SerializedName("humid_pct")
    var humidPct: Int = 0

    @SerializedName("windspd_mph")
    var windspdMph: Double = 0.0

    @SerializedName("windspd_kmh")
    var windspdKmh: Int = 0

    @SerializedName("windspd_kts")
    var windspdKts: Double = 0.0

    @SerializedName("windspd_ms")
    var windspdMS: Double = 0.0

    @SerializedName("winddir_deg")
    var winddirDeg: Int = 0

    @SerializedName("winddir_compass")
    var winddirCompass: String = ""

    @SerializedName("cloudtotal_pct")
    var cloudtotalPct: Int = 0

    @SerializedName("vis_km")
    var visKM: Int = 0

    @SerializedName("vis_mi")
    var visMi: Double = 0.0

    @SerializedName("slp_mb")
    var slpMB: Double = 0.0

    @SerializedName("slp_in")
    var slpIn: Double = 0.0

    @SerializedName("dewpoint_c")
    var dewpointC: Double = 0.0

    @SerializedName("dewpoint_f")
    var dewpointF: Double = 0.0
}
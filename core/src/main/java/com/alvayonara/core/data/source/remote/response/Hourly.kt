package com.alvayonara.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("clouds")
    val clouds: String? = null,
    @SerializedName("dew_point")
    val dewPoint: String? = null,
    @SerializedName("dt")
    val dt: String? = null,
    @SerializedName("feels_like")
    val feelsLike: String? = null,
    @SerializedName("humidity")
    val humidity: String? = null,
    @SerializedName("pop")
    val pop: String? = null,
    @SerializedName("pressure")
    val pressure: String? = null,
    @SerializedName("rain")
    val rain: Rain? = null,
    @SerializedName("temp")
    val temp: String? = null,
    @SerializedName("uvi")
    val uvi: String? = null,
    @SerializedName("visibility")
    val visibility: String? = null,
    @SerializedName("weather")
    val weather: List<WeatherXX>? = null,
    @SerializedName("wind_deg")
    val windDeg: String? = null,
    @SerializedName("wind_gust")
    val windGust: String? = null,
    @SerializedName("wind_speed")
    val windSpeed: String? = null
)
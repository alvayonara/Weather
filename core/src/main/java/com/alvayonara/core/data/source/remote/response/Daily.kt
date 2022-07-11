package com.alvayonara.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("clouds")
    val clouds: String? = null,
    @SerializedName("dew_point")
    val dewPoint: String? = null,
    @SerializedName("dt")
    val dt: String? = null,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike? = null,
    @SerializedName("humidity")
    val humidity: String? = null,
    @SerializedName("moon_phase")
    val moonPhase: String? = null,
    @SerializedName("moonrise")
    val moonrise: String? = null,
    @SerializedName("moonset")
    val moonset: String? = null,
    @SerializedName("pop")
    val pop: String? = null,
    @SerializedName("pressure")
    val pressure: String? = null,
    @SerializedName("rain")
    val rain: String? = null,
    @SerializedName("sunrise")
    val sunrise: String? = null,
    @SerializedName("sunset")
    val sunset: String? = null,
    @SerializedName("temp")
    val temp: Temp? = null,
    @SerializedName("uvi")
    val uvi: String? = null,
    @SerializedName("weather")
    val weather: List<WeatherX>? = null,
    @SerializedName("wind_deg")
    val windDeg: String? = null,
    @SerializedName("wind_gust")
    val windGust: String? = null,
    @SerializedName("wind_speed")
    val windSpeed: String? = null
)
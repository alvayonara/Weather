package com.alvayonara.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: Current? = null,
    @SerializedName("daily")
    val daily: List<Daily>? = null,
    @SerializedName("hourly")
    val hourly: List<Hourly>? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("minutely")
    val minutely: List<Minutely>? = null,
    @SerializedName("timezone")
    val timezone: String? = null,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int? = null
)
package com.alvayonara.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class WeatherX(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("icon")
    val icon: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("main")
    val main: String? = null
)
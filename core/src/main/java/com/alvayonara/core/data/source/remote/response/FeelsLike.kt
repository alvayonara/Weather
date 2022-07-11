package com.alvayonara.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class FeelsLike(
    @SerializedName("day")
    val day: String? = null,
    @SerializedName("eve")
    val eve: String? = null,
    @SerializedName("morn")
    val morn: String? = null,
    @SerializedName("night")
    val night: String? = null
)
package com.alvayonara.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class Minutely(
    @SerializedName("dt")
    val dt: Int? = null,
    @SerializedName("precipitation")
    val precipitation: Int? = null
)
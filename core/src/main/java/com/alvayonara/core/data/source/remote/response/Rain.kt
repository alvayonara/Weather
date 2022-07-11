package com.alvayonara.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: String? = null
)
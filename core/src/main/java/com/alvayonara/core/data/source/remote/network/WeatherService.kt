package com.alvayonara.core.data.source.remote.network

import com.alvayonara.core.data.source.remote.response.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")
    fun getWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") appId: String
    ): Observable<WeatherResponse>
}
package com.alvayonara.core.domain.repository

import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.data.source.remote.response.WeatherResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface WeatherRepository {
    fun getWeather(latitude: String, longitude: String, appId: String): Observable<WeatherResponse>
    fun getWeatherById(id: Int): Observable<WeatherEntity>
    fun getAllWeather(): Observable<List<WeatherEntity>>
    fun insertWeather(weatherEntity: WeatherEntity): Single<Long>
    fun insertAllWeather(weathers: List<WeatherEntity>): Single<List<Long>>
    fun deleteWeather(latitude: String, longitude: String): Single<Int>
    fun deleteAllWeather(): Single<Int>
    fun isLocationExist(location: String): Observable<Boolean>
    fun isWeatherExist(): Observable<Boolean>
    fun updateWeather(weatherEntity: WeatherEntity): Single<Int>
}
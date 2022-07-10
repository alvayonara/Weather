package com.alvayonara.core.domain.repository

import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.data.source.remote.response.WeatherResponse
import io.reactivex.Completable
import io.reactivex.Observable

interface WeatherRepository {
    fun getWeather(latitude: String, longitude: String, appId: String): Observable<WeatherResponse>
    fun getAllWeather(): Observable<List<WeatherEntity>>
    fun insertWeather(weatherEntity: WeatherEntity): Completable
    fun insertAllWeather(weathers: List<WeatherEntity>): Completable
    fun deleteWeather(latitude: String, longitude: String): Completable
    fun deleteAllWeather(): Completable
    fun isLocationExist(location: String): Observable<Boolean>
}
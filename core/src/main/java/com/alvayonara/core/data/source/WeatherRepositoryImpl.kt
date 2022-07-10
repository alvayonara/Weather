package com.alvayonara.core.data.source

import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.data.source.local.room.WeatherDao
import com.alvayonara.core.data.source.remote.network.WeatherService
import com.alvayonara.core.data.source.remote.response.WeatherResponse
import com.alvayonara.core.domain.repository.WeatherRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService,
    private val weatherDao: WeatherDao
) : WeatherRepository {
    override fun getWeather(
        latitude: String,
        longitude: String,
        appId: String
    ): Observable<WeatherResponse> = weatherService.getWeather(latitude, longitude, appId)

    override fun getAllWeather(): Observable<List<WeatherEntity>> = weatherDao.getAllWeather()

    override fun insertWeather(weatherEntity: WeatherEntity): Completable =
        weatherDao.insertWeather(weatherEntity)

    override fun insertAllWeather(weathers: List<WeatherEntity>): Completable =
        weatherDao.insertAllWeather(weathers)

    override fun deleteWeather(latitude: String, longitude: String): Completable =
        weatherDao.deleteWeather(latitude, longitude)

    override fun deleteAllWeather(): Completable = weatherDao.deleteAllWeather()

    override fun isLocationExist(location: String): Observable<Boolean> =
        weatherDao.isLocationExist(location)
}
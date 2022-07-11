package com.alvayonara.core.data.source

import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.data.source.local.room.WeatherDao
import com.alvayonara.core.data.source.remote.network.WeatherService
import com.alvayonara.core.data.source.remote.response.WeatherResponse
import com.alvayonara.core.domain.repository.WeatherRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
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

    override fun getWeatherById(id: Int): Observable<WeatherEntity> = weatherDao.getWeatherById(id)

    override fun getAllWeather(): Observable<List<WeatherEntity>> = weatherDao.getAllWeather()

    override fun insertWeather(weatherEntity: WeatherEntity): Single<Long> =
        weatherDao.insertWeather(weatherEntity)

    override fun insertAllWeather(weathers: List<WeatherEntity>): Single<List<Long>> =
        weatherDao.insertAllWeather(weathers)

    override fun deleteWeather(latitude: String, longitude: String): Single<Int> =
        weatherDao.deleteWeather(latitude, longitude)

    override fun deleteAllWeather(): Single<Int> = weatherDao.deleteAllWeather()

    override fun isLocationExist(location: String): Observable<Boolean> =
        weatherDao.isLocationExist(location)

    override fun isWeatherExist(): Observable<Boolean> =
        weatherDao.isWeatherExist()

    override fun updateWeather(weatherEntity: WeatherEntity): Single<Int> =
        weatherDao.updateWeather(weatherEntity)
}
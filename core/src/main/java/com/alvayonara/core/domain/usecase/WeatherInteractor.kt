package com.alvayonara.core.domain.usecase

import com.alvayonara.core.BuildConfig
import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.data.source.remote.response.WeatherResponse
import com.alvayonara.core.domain.repository.WeatherRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class WeatherInteractor @Inject constructor(private val weatherRepository: WeatherRepository) :
    WeatherUseCase {
    override fun getWeather(
        latitude: String,
        longitude: String
    ): Observable<WeatherResponse> = weatherRepository.getWeather(latitude, longitude, BuildConfig.API_KEY)

    override fun getWeatherById(id: Int): Observable<WeatherEntity> = weatherRepository.getWeatherById(id)

    override fun getAllWeather(): Observable<List<WeatherEntity>> =
        weatherRepository.getAllWeather()

    override fun insertWeather(weatherEntity: WeatherEntity): Single<Long> =
        weatherRepository.insertWeather(weatherEntity)

    override fun insertAllWeather(weathers: List<WeatherEntity>): Single<List<Long>> =
        weatherRepository.insertAllWeather(weathers)

    override fun deleteWeather(latitude: String, longitude: String): Single<Int> =
        weatherRepository.deleteWeather(latitude, longitude)

    override fun deleteAllWeather(): Single<Int> = weatherRepository.deleteAllWeather()

    override fun isLocationExist(location: String): Observable<Boolean> =
        weatherRepository.isLocationExist(location)

    override fun isWeatherExist(): Observable<Boolean> =
        weatherRepository.isWeatherExist()

    override fun updateWeather(weatherEntity: WeatherEntity): Single<Int> =
        weatherRepository.updateWeather(weatherEntity)
}
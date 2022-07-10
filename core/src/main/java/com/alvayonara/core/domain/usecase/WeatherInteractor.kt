package com.alvayonara.core.domain.usecase

import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.data.source.remote.response.WeatherResponse
import com.alvayonara.core.domain.repository.WeatherRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class WeatherInteractor @Inject constructor(private val weatherRepository: WeatherRepository) :
    WeatherUseCase {
    override fun getWeather(
        latitude: String,
        longitude: String,
        appId: String
    ): Observable<WeatherResponse> = weatherRepository.getWeather(latitude, longitude, appId)

    override fun getAllWeather(): Observable<List<WeatherEntity>> =
        weatherRepository.getAllWeather()

    override fun insertWeather(weatherEntity: WeatherEntity): Completable =
        weatherRepository.insertWeather(weatherEntity)

    override fun insertAllWeather(weathers: List<WeatherEntity>): Completable =
        weatherRepository.insertAllWeather(weathers)

    override fun deleteWeather(latitude: String, longitude: String): Completable =
        weatherRepository.deleteWeather(latitude, longitude)

    override fun deleteAllWeather(): Completable = weatherRepository.deleteAllWeather()

    override fun isLocationExist(location: String): Observable<Boolean> =
        weatherRepository.isLocationExist(location)
}
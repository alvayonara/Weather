package com.alvayonara.weather.utils

import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.data.source.remote.response.WeatherResponse

object WeatherMapper {

    fun WeatherResponse.mapWeatherResponseToEntity(location: String): WeatherEntity =
        WeatherEntity(
            location = location,
            latitude = this.lat.toString(),
            longitude = this.lon.toString(),
            currentWeather = this.current?.weather?.firstOrNull()?.main,
            temperature = this.current?.temp.toString(),
            humidity = this.current?.humidity.toString(),
            windSpeed = this.current?.windSpeed.toString(),
            pressure = this.current?.pressure.toString()
        )
}
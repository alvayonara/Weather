package com.alvayonara.weather.di

import com.alvayonara.core.domain.usecase.WeatherInteractor
import com.alvayonara.core.domain.usecase.WeatherUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun provideWeatherUseCase(weatherInteractor: WeatherInteractor): WeatherUseCase
}
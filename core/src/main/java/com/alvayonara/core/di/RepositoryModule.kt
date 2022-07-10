package com.alvayonara.core.di

import com.alvayonara.core.data.source.WeatherRepositoryImpl
import com.alvayonara.core.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class])
abstract class RepositoryModule {

    @Binds
    abstract fun provideWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}
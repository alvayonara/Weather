package com.alvayonara.core.di

import android.content.Context
import androidx.room.Room
import com.alvayonara.core.data.source.local.room.WeatherDao
import com.alvayonara.core.data.source.local.room.WeatherDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): WeatherDatabase = Room.databaseBuilder(
        context,
        WeatherDatabase::class.java, "Weather.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao = weatherDatabase.weatherDao()
}
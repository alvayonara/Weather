package com.alvayonara.core.data.source.local.room

import androidx.room.*
import com.alvayonara.core.data.source.local.entity.WeatherEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getAllWeather(): Observable<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherEntity: WeatherEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWeather(weathers: List<WeatherEntity>): Single<List<Long>>

    @Query("DELETE FROM weather WHERE latitude = :latitude AND longitude = :longitude")
    fun deleteWeather(latitude: String, longitude: String): Single<Int>

    @Query("DELETE FROM weather")
    fun deleteAllWeather(): Single<Int>

    @Query("SELECT EXISTS(SELECT * FROM weather WHERE location = :location)")
    fun isLocationExist(location: String): Observable<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM weather)")
    fun isWeatherExist(): Observable<Boolean>

    @Update
    fun updateWeather(weatherEntity: WeatherEntity): Single<Int>
}
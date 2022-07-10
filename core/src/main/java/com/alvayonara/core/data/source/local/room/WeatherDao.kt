package com.alvayonara.core.data.source.local.room

import androidx.room.*
import com.alvayonara.core.data.source.local.entity.WeatherEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getAllWeather(): Observable<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherEntity: WeatherEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWeather(weathers: List<WeatherEntity>): Completable

    @Query("DELETE FROM weather WHERE latitude = :latitude AND longitude = :longitude")
    fun deleteWeather(latitude: String, longitude: String): Completable

    @Query("DELETE FROM weather")
    fun deleteAllWeather(): Completable

    @Query("SELECT EXISTS(SELECT * FROM weather WHERE location = :location)")
    fun isLocationExist(location: String): Observable<Boolean>
}
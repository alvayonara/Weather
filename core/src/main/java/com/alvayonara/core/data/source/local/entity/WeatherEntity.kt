package com.alvayonara.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int? = 0,
    @ColumnInfo(name = "location")
    var location: String? = "",
    @ColumnInfo(name = "latitude")
    var latitude: String? = "",
    @ColumnInfo(name = "longitude")
    var longitude: String? = "",
    @ColumnInfo(name = "current_weather")
    var currentWeather: String? = "",
    @ColumnInfo(name = "temperature")
    var temperature: String? = "",
    @ColumnInfo(name = "humidity")
    var humidity: String? = "",
    @ColumnInfo(name = "wind_speed")
    var windSpeed: String? = "",
    @ColumnInfo(name = "pressure")
    var pressure: String? = ""
): Parcelable
package com.alvayonara.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alvayonara.core.data.source.local.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}
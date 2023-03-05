package com.ahk.satfinder.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahk.satfinder.core.data.model.SatelliteDetail

@Database(
    entities = [SatelliteDetail::class],
    version = 1,
    exportSchema = false,
)
abstract class SatelliteDB : RoomDatabase() {
    abstract fun satelliteDetailDAO(): SatelliteDetailDAO
}

package com.ahk.satfinder.core.data.di

import android.content.Context
import androidx.room.Room
import com.ahk.satfinder.core.data.assets.AssetService
import com.ahk.satfinder.core.data.assets.AssetServiceImpl
import com.ahk.satfinder.core.data.db.SatelliteDB
import com.ahk.satfinder.core.data.db.SatelliteDetailDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideAssetService(@ApplicationContext context: Context): AssetService =
        AssetServiceImpl(context.assets)

    @Provides
    @Singleton
    fun provideSatelliteDB(@ApplicationContext context: Context): SatelliteDB =
        Room.databaseBuilder(context, SatelliteDB::class.java, "satellite.db").build()

    @Provides
    @Singleton
    fun provideSatelliteDetailDAO(satelliteDB: SatelliteDB): SatelliteDetailDAO =
        satelliteDB.satelliteDetailDAO()
}

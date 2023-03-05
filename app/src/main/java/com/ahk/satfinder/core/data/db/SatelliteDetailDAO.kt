package com.ahk.satfinder.core.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ahk.satfinder.core.data.model.SatelliteDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface SatelliteDetailDAO {

    @Query("SELECT * FROM satellite_detail WHERE id = :id")
    fun getOne(id: Int): Single<List<SatelliteDetail>>

    @Upsert
    fun upsert(satelliteDetail: SatelliteDetail): Completable
}

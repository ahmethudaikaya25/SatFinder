package com.ahk.satfinder.core.domain.detail

import com.ahk.satfinder.core.data.model.SatelliteDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface DatabaseRepository {
    fun getSatelliteDetailFromDB(id: Int): Single<List<SatelliteDetail>>
    fun upsertSatelliteDetail(satelliteDetail: SatelliteDetail): Completable
}

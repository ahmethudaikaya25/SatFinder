package com.ahk.satfinder.core.domain.detail

import com.ahk.satfinder.core.data.db.SatelliteDetailDAO
import com.ahk.satfinder.core.data.model.SatelliteDetail
import io.reactivex.rxjava3.core.Completable

class DatabaseRepositoryImpl(
    private val satelliteDetailDAO: SatelliteDetailDAO,
) : DatabaseRepository {
    override fun getSatelliteDetailFromDB(id: Int) =
        satelliteDetailDAO.getOne(id)

    override fun upsertSatelliteDetail(satelliteDetail: SatelliteDetail): Completable =
        satelliteDetailDAO.upsert(satelliteDetail)
}

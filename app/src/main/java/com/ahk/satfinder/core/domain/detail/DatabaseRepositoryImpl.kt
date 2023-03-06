package com.ahk.satfinder.core.domain.detail

import com.ahk.satfinder.core.data.db.SatelliteDetailDAO
import com.ahk.satfinder.core.data.model.DataLayerException
import com.ahk.satfinder.core.data.model.SatelliteDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class DatabaseRepositoryImpl(
    private val satelliteDetailDAO: SatelliteDetailDAO,
) : DatabaseRepository {
    override fun getSatelliteDetailFromDB(id: Int) =
        satelliteDetailDAO.getOne(id)
            .onErrorResumeNext {
                Single.error(DataLayerException.DatabaseCouldNotBeRead("Database read error"))
            }

    override fun upsertSatelliteDetail(satelliteDetail: SatelliteDetail): Completable =
        satelliteDetailDAO.upsert(satelliteDetail)
            .onErrorResumeNext {
                Completable.error(DataLayerException.DatabaseCouldNotBeWritten("Database write error"))
            }
}

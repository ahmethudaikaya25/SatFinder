package com.ahk.satfinder.core.domain.detail

import com.ahk.satfinder.core.data.model.SatelliteDetail
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.ahk.satfinder.core.domain.assets.AssetRepository
import com.ahk.satfinder.core.domain.util.DomainLayerException
import io.reactivex.rxjava3.core.Single

class GetDetailUseCaseImpl(
    private val databaseRepository: DatabaseRepository,
    private val assetRepository: AssetRepository,
) : GetDetailUseCase {
    override fun invoke(satelliteSummary: SatelliteSummary): Single<SatelliteDetail> {
        val id = satelliteSummary.id
        return databaseRepository.getSatelliteDetailFromDB(id)
            .onErrorResumeNext {
                Single.just(emptyList())
            }
            .flatMap {
                return@flatMap if (it.isEmpty()) {
                    assetRepository.getSatelliteDetailList()
                        .flatMap { details ->
                            val filteredSatellites = details.filter { detail -> detail.id == id }
                            if (filteredSatellites.isEmpty()) {
                                return@flatMap Single.error<SatelliteDetail>(
                                    DomainLayerException.DetailNotFoundException("Detail not found"),
                                )
                            }
                            val satellite = filteredSatellites[0]
                            satellite.name = satelliteSummary.name
                            println(satellite)
                            databaseRepository.upsertSatelliteDetail(satellite)
                                .toSingle {
                                    satellite
                                }
                        }
                } else {
                    return@flatMap Single.just(it[0])
                }
            }
    }
}

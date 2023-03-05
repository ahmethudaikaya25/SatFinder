package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.core.data.model.SatelliteSummary
import io.reactivex.rxjava3.core.Single

class FilterAssetsUseCaseImpl(
    private val assetRepository: AssetRepository,
) : FilterAssetsUseCase {
    override fun invoke(query: String): Single<List<SatelliteSummary>> {
        if (query.length < 3) {
            return assetRepository.getSatelliteList()
        }
        return assetRepository.getSatelliteList()
            .map { list ->
                list.filter { it.name.contains(query, true) }
            }
    }
}

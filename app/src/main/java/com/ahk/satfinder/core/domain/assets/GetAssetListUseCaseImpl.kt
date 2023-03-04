package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.core.data.model.SatelliteSummary
import io.reactivex.rxjava3.core.Single

class GetAssetListUseCaseImpl(
    private val assetRepository: AssetRepository,
) : GetAssetListUseCase {
    override fun invoke(): Single<List<SatelliteSummary>> = assetRepository.getSatelliteList()
}

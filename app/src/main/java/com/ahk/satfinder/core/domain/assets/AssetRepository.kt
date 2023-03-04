package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.core.data.model.SatelliteSummary
import io.reactivex.rxjava3.core.Single

interface AssetRepository {
    fun getSatelliteList(): Single<List<SatelliteSummary>>
}

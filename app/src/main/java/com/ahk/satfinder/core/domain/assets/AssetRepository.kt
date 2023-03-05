package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.core.data.model.Positions
import com.ahk.satfinder.core.data.model.SatelliteDetail
import com.ahk.satfinder.core.data.model.SatelliteSummary
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface AssetRepository {
    fun getSatelliteList(): Single<List<SatelliteSummary>>
    fun getSatelliteDetailList(): Single<List<SatelliteDetail>>
    fun getPositions(): Observable<List<Positions>>
}

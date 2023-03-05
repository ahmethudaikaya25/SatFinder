package com.ahk.satfinder.core.domain.detail

import com.ahk.satfinder.core.data.model.SatelliteDetail
import com.ahk.satfinder.core.data.model.SatelliteSummary
import io.reactivex.rxjava3.core.Single

interface GetDetailUseCase {
    fun invoke(satelliteSummary: SatelliteSummary): Single<SatelliteDetail>
}

package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.core.data.model.SatelliteSummary
import io.reactivex.rxjava3.core.Single

interface FilterAssetsUseCase {
    fun invoke(query: String): Single<List<SatelliteSummary>>
}

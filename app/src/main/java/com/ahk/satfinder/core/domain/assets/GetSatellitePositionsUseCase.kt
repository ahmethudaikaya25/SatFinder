package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.core.data.model.Position
import io.reactivex.rxjava3.core.Observable

interface GetSatellitePositionsUseCase {
    fun invoke(id: Int): Observable<List<Position>>
}
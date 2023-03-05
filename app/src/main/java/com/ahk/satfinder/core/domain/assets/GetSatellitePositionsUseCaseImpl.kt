package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.core.data.model.Position
import io.reactivex.rxjava3.core.Observable

class GetSatellitePositionsUseCaseImpl(
    private val assetRepository: AssetRepository,
) : GetSatellitePositionsUseCase {
    override fun invoke(id: Int): Observable<List<Position>> =
        assetRepository.getPositions()
            .flatMap { positions ->
                val positionOfId = positions.filter { position -> position.id == id }[0]
                return@flatMap Observable.just(positionOfId.positions)
            }
}

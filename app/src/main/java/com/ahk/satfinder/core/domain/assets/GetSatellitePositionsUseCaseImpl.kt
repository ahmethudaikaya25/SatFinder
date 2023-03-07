package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.core.data.model.Position
import com.ahk.satfinder.core.domain.util.DomainLayerException
import io.reactivex.rxjava3.core.Observable

class GetSatellitePositionsUseCaseImpl(
    private val assetRepository: AssetRepository,
) : GetSatellitePositionsUseCase {
    override fun invoke(id: Int): Observable<List<Position>> =
        assetRepository.getPositions()
            .flatMap { positions ->
                val positionOfId = positions.filter { position -> position.id == id }
                if (positionOfId.isEmpty()) {
                    return@flatMap Observable.error(
                        DomainLayerException.ThereIsNoPositionInformation(
                            "There is no position information of this satellite",
                        ),
                    )
                }
                return@flatMap Observable.just(positionOfId[0].positions)
            }
}

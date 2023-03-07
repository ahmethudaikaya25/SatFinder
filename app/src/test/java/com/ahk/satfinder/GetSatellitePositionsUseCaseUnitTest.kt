package com.ahk.satfinder

import com.ahk.satfinder.core.data.assets.AssetService
import com.ahk.satfinder.core.data.model.DataLayerException
import com.ahk.satfinder.core.data.model.Position
import com.ahk.satfinder.core.data.model.Positions
import com.ahk.satfinder.core.domain.assets.AssetRepository
import com.ahk.satfinder.core.domain.assets.AssetRepositoryImpl
import com.ahk.satfinder.core.domain.assets.GetSatellitePositionsUseCase
import com.ahk.satfinder.core.domain.assets.GetSatellitePositionsUseCaseImpl
import com.ahk.satfinder.core.domain.util.DomainLayerException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import org.mockito.Mockito

class GetSatellitePositionsUseCaseUnitTest {
    @Test
    fun whenIdIsOne_returnPositionsOfIdOne() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        val positionList1 = listOf(
            Position(1.0, 2.0),
            Position(3.0, 4.0),
            Position(5.0, 6.0),
        )
        val positionList2 = listOf(
            Position(7.0, 8.0),
            Position(9.0, 10.0),
            Position(11.0, 12.0),
        )
        val positionList3 = listOf(
            Position(13.0, 14.0),
            Position(15.0, 16.0),
            Position(17.0, 18.0),
        )
        Mockito.`when`(assetRepository.getPositions()).thenReturn(
            Observable.just(
                listOf(
                    Positions(1, positionList1),
                    Positions(2, positionList2),
                    Positions(3, positionList3),
                ),
            ),
        )
        val getSatellitePositionsUseCase: GetSatellitePositionsUseCase =
            GetSatellitePositionsUseCaseImpl(assetRepository)
        getSatellitePositionsUseCase.invoke(1).test().assertValue(positionList1)
    }

    @Test
    fun whenPositionNotFoundForId_returnError() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        val positionList1 = listOf(
            Position(1.0, 2.0),
            Position(3.0, 4.0),
            Position(5.0, 6.0),
        )
        Mockito.`when`(assetRepository.getPositions()).thenReturn(
            Observable.just(
                listOf(
                    Positions(1, positionList1),
                ),
            ),
        )
        val getSatellitePositionsUseCase: GetSatellitePositionsUseCase =
            GetSatellitePositionsUseCaseImpl(assetRepository)
        getSatellitePositionsUseCase.invoke(2)
            .test()
            .assertError(DomainLayerException.ThereIsNoPositionInformation::class.java)
    }

    @Test
    fun whenPositionListIsEmpty_returnError() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getPositions()).thenReturn(
            Observable.just(
                listOf(),
            ),
        )
        val getSatellitePositionsUseCase: GetSatellitePositionsUseCase =
            GetSatellitePositionsUseCaseImpl(assetRepository)
        getSatellitePositionsUseCase.invoke(1)
            .test()
            .assertError(DomainLayerException.ThereIsNoPositionInformation::class.java)
    }

    @Test
    fun whenRepositoryReturnsParseError_returnParseError() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getPositions()).thenReturn(
            Observable.error(
                DomainLayerException.AssetCouldNotBeParsed(
                    "Position fetching error",
                ),
            ),
        )
        val getSatellitePositionsUseCase: GetSatellitePositionsUseCase =
            GetSatellitePositionsUseCaseImpl(assetRepository)
        getSatellitePositionsUseCase.invoke(1)
            .test()
            .assertError(DomainLayerException.AssetCouldNotBeParsed::class.java)
    }

    @Test
    fun whenRepositoryReturnFileReadError_returnFileReadError() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getPositions()).thenReturn(
            Observable.error(
                DataLayerException.FileCouldNotBeRead(
                    "File read error",
                ),
            ),
        )
        val getSatellitePositionsUseCase: GetSatellitePositionsUseCase =
            GetSatellitePositionsUseCaseImpl(assetRepository)
        getSatellitePositionsUseCase.invoke(1)
            .test()
            .assertError(DataLayerException.FileCouldNotBeRead::class.java)
    }

    @Test
    fun whenAssetServiceReturnsCorrectJson_returnListPositions() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.POSITION))
            .thenReturn(
                Single.just(
                    """
                        {
                          "list": [
                            {
                              "id": "1",
                              "positions": [
                                {
                                  "posX": 0.864328541,
                                  "posY": 0.646450811
                                },
                                {
                                  "posX": 0.459465488,
                                  "posY": 0.323434385
                                },
                                {
                                  "posX": 0.213733842,
                                  "posY": 0.239480213
                                }
                              ]
                            },
                            {
                              "id": "2",
                              "positions": [
                                {
                                  "posX": 0.864328522,
                                  "posY": 0.646450855
                                },
                                {
                                  "posX": 0.459465477,
                                  "posY": 0.323434315
                                },
                                {
                                  "posX": 0.213733123,
                                  "posY": 0.239480024
                                }
                              ]
                            },
                            {
                              "id": "3",
                              "positions": [
                                {
                                  "posX": 0.864328328,
                                  "posY": 0.646450102
                                },
                                {
                                  "posX": 0.459465503,
                                  "posY": 0.323434183
                                },
                                {
                                  "posX": 0.213733825,
                                  "posY": 0.239480259
                                }
                              ]
                            }
                          ]
                        }
                    """.trimIndent(),
                ),
            )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getPositions().test().assertValue(
            listOf(
                Positions(
                    1,
                    listOf(
                        Position(0.864328541, 0.646450811),
                        Position(0.459465488, 0.323434385),
                        Position(0.213733842, 0.239480213),
                    ),
                ),
                Positions(
                    2,
                    listOf(
                        Position(0.864328522, 0.646450855),
                        Position(0.459465477, 0.323434315),
                        Position(0.213733123, 0.239480024),
                    ),
                ),
                Positions(
                    3,
                    listOf(
                        Position(0.864328328, 0.646450102),
                        Position(0.459465503, 0.323434183),
                        Position(0.213733825, 0.239480259),
                    ),
                ),
            ),
        )
    }

    @Test
    fun whenAssetServiceReturnsEmptyJson_returnEmptyList() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.POSITION))
            .thenReturn(
                Single.just(
                    """
                        {
                          "list": []
                        }
                    """.trimIndent(),
                ),
            )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getPositions().test().assertValue(listOf())
    }

    @Test
    fun whenAssetServiceReturnsCantParsedJson_returnAssetCouldNotBeParsed() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.POSITION))
            .thenReturn(
                Single.just(
                    """
                        [
                          "list": []
                        }
                    """.trimIndent(),
                ),
            )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getPositions().test()
            .assertError(DomainLayerException.AssetCouldNotBeParsed::class.java)
    }

    @Test
    fun whenAssetServiceReturnsCantReadFile_returnFileCouldNotBeRead() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.POSITION))
            .thenReturn(
                Single.error(
                    DataLayerException.FileCouldNotBeRead("File could not be read"),
                ),
            )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getPositions().test()
            .assertError(DataLayerException.FileCouldNotBeRead::class.java)
    }

    @Test
    fun whenAssetServiceReturnsEmptyString_returnCouldNotParsed() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.POSITION))
            .thenReturn(
                Single.just(""),
            )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getPositions().test()
            .assertError(DomainLayerException.AssetCouldNotBeParsed::class.java)
    }
}

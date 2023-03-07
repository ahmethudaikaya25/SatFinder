package com.ahk.satfinder

import com.ahk.satfinder.core.data.assets.AssetService
import com.ahk.satfinder.core.data.model.DataLayerException
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.ahk.satfinder.core.domain.assets.AssetRepository
import com.ahk.satfinder.core.domain.assets.AssetRepositoryImpl
import com.ahk.satfinder.core.domain.assets.FilterAssetsUseCase
import com.ahk.satfinder.core.domain.assets.FilterAssetsUseCaseImpl
import com.ahk.satfinder.core.domain.util.DomainLayerException
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import org.mockito.Mockito

class FilterAssetsUseCaseUnitTest {

    @Test
    fun whenQueryIsEmpty_returnAllAssets() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getSatelliteList()).thenReturn(
            Single.just(
                listOf(
                    SatelliteSummary(1, true, "name1"),
                    SatelliteSummary(2, false, "name2"),
                    SatelliteSummary(3, true, "name3"),
                ),
            ),
        )
        val filAssetUC: FilterAssetsUseCase = FilterAssetsUseCaseImpl(assetRepository)
        filAssetUC.invoke("").test().assertValue(
            listOf(
                SatelliteSummary(1, true, "name1"),
                SatelliteSummary(2, false, "name2"),
                SatelliteSummary(3, true, "name3"),
            ),
        )
    }

    @Test
    fun whenQuerySizeSmallerThenThree_returnAllList() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getSatelliteList()).thenReturn(
            Single.just(
                listOf(
                    SatelliteSummary(1, true, "name1"),
                    SatelliteSummary(2, false, "name2"),
                    SatelliteSummary(3, true, "name3"),
                ),
            ),
        )
        val filAssetUC: FilterAssetsUseCase = FilterAssetsUseCaseImpl(assetRepository)
        filAssetUC.invoke("de").test().assertValue(
            listOf(
                SatelliteSummary(1, true, "name1"),
                SatelliteSummary(2, false, "name2"),
                SatelliteSummary(3, true, "name3"),
            ),
        )
    }

    @Test
    fun whenQuerySizeBiggerThenThree_returnFilteredAssets() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getSatelliteList()).thenReturn(
            Single.just(
                listOf(
                    SatelliteSummary(1, true, "differentName1"),
                    SatelliteSummary(2, false, "notDifferentName2"),
                    SatelliteSummary(3, true, "differentName3"),
                ),
            ),
        )
        val filAssetUC: FilterAssetsUseCase = FilterAssetsUseCaseImpl(assetRepository)
        filAssetUC.invoke("not").test().assertValue(
            listOf(
                SatelliteSummary(2, false, "notDifferentName2"),
            ),
        )
    }

    @Test
    fun whenRepositoryReturnsParseError_returnParseError() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getSatelliteList()).thenReturn(
            Single.error(
                DomainLayerException.AssetCouldNotBeParsed(
                    "Could not parse json",
                ),
            ),
        )
        val filAssetUC: FilterAssetsUseCase = FilterAssetsUseCaseImpl(assetRepository)
        filAssetUC.invoke("not").test().assertError(
            DomainLayerException.AssetCouldNotBeParsed::class.java,
        )
    }

    @Test
    fun whenRepositoryReturnsFileReadError_returnFileReadError() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getSatelliteList()).thenReturn(
            Single.error(
                DataLayerException.FileCouldNotBeRead("File read error"),
            ),
        )
        val filAssetUC: FilterAssetsUseCase = FilterAssetsUseCaseImpl(assetRepository)
        filAssetUC.invoke("not").test().assertError(
            DataLayerException.FileCouldNotBeRead::class.java,
        )
    }

    @Test
    fun whenServiceReturnsFileReadError_returnFileReadError() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.SUMMARY)).thenReturn(
            Single.error(
                DataLayerException.FileCouldNotBeRead("File read error"),
            ),
        )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getSatelliteList().test().assertError(
            DataLayerException.FileCouldNotBeRead::class.java,
        )
    }

    @Test
    fun whenServiceReturnsCorrectJsonString_returnList() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.SUMMARY)).thenReturn(
            Single.just(
                """
                [
                    {
                        "id": 1,
                        "active": true,
                        "name": "name1"
                    },
                    {
                        "id": 2,
                        "active": false,
                        "name": "name2"
                    },
                    {
                        "id": 3,
                        "active": true,
                        "name": "name3"
                    }
                ]
                """.trimIndent(),
            ),
        )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getSatelliteList().test().assertValue(
            listOf(
                SatelliteSummary(1, true, "name1"),
                SatelliteSummary(2, false, "name2"),
                SatelliteSummary(3, true, "name3"),
            ),
        )
    }

    @Test
    fun whenServiceReturnsIncorrectJsonString_returnParseError() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.SUMMARY)).thenReturn(
            Single.just(
                """
                [
                  {
                    "id": 1,
                    "active": false,
                    "name": "Starship-1"
                  }
                  {
                    "id": 2,
                    "active": true,
                    "name": "Dragon-1"
                  ,
                  
                    "id": 3,
                    "active": true,
                    "name": "Starship-3"
                  
                ]
                """.trimIndent(),
            ),
        )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getSatelliteList().test().assertError(
            DomainLayerException.AssetCouldNotBeParsed::class.java,
        )
    }
}

package com.ahk.satfinder

import com.ahk.satfinder.core.data.assets.AssetService
import com.ahk.satfinder.core.data.model.DataLayerException
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.ahk.satfinder.core.domain.assets.AssetRepository
import com.ahk.satfinder.core.domain.assets.AssetRepositoryImpl
import com.ahk.satfinder.core.domain.assets.GetSatelliteListUseCase
import com.ahk.satfinder.core.domain.assets.GetSatelliteListUseCaseImpl
import com.ahk.satfinder.core.domain.util.DomainLayerException
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import org.mockito.Mockito

class GetSatelliteListUseCaseUnitTest {

    @Test
    fun whenRepositoryReturnsSummaryList_thenUseCaseReturnsSummaryList() {
        val satelliteSummaryList = listOf(
            SatelliteSummary(
                1,
                true,
                "name1",
            ),
            SatelliteSummary(
                2,
                false,
                "name2",
            ),
            SatelliteSummary(
                3,
                true,
                "name3",
            ),
        )
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getSatelliteList())
            .thenReturn(Single.just(satelliteSummaryList))
        val getSatelliteListUseCase: GetSatelliteListUseCase =
            GetSatelliteListUseCaseImpl(assetRepository)
        getSatelliteListUseCase.invoke()
            .test()
            .assertValue(satelliteSummaryList)
    }

    @Test
    fun whenRepositoryReturnsEmptySummaryList_thenUseCaseReturnsEmptySummaryList() {
        val satelliteSummaryList = emptyList<SatelliteSummary>()
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getSatelliteList())
            .thenReturn(Single.just(satelliteSummaryList))
        val getSatelliteListUseCase: GetSatelliteListUseCase =
            GetSatelliteListUseCaseImpl(assetRepository)
        getSatelliteListUseCase.invoke()
            .test()
            .assertValue(satelliteSummaryList)
    }

    @Test
    fun whenRepositoryReturnsFileCouldNotBeReadError_thenUseCaseReturnsFileCouldNotBeReadError() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getSatelliteList())
            .thenReturn(Single.error(DataLayerException.FileCouldNotBeRead("File could not be read")))
        val getSatelliteListUseCase: GetSatelliteListUseCase =
            GetSatelliteListUseCaseImpl(assetRepository)
        getSatelliteListUseCase.invoke()
            .test()
            .assertError(DataLayerException.FileCouldNotBeRead::class.java)
    }

    @Test
    fun whenRepositoryReturnsFileCouldNotBeParsedError_thenUseCaseReturnsFileCouldNotBeParsedError() {
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        Mockito.`when`(assetRepository.getSatelliteList())
            .thenReturn(Single.error(DomainLayerException.AssetCouldNotBeParsed("File could not be parsed")))
        val getSatelliteListUseCase: GetSatelliteListUseCase =
            GetSatelliteListUseCaseImpl(assetRepository)
        getSatelliteListUseCase.invoke()
            .test()
            .assertError(DomainLayerException.AssetCouldNotBeParsed::class.java)
    }

    @Test
    fun whenServiceReturnsFileCouldNotBeRead_thenRepositoryReturnsFileCouldNotBeReadError() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.SUMMARY)).thenReturn(
            Single.error(DataLayerException.FileCouldNotBeRead("File could not be read")),
        )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getSatelliteList()
            .test()
            .assertError(DataLayerException.FileCouldNotBeRead::class.java)
    }

    @Test
    fun whenServiceReturnsBlankContent_thenRepositoryReturnsFileCouldNotBeParsedError() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.SUMMARY)).thenReturn(
            Single.just(""),
        )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getSatelliteList()
            .test()
            .assertError(DomainLayerException.AssetCouldNotBeParsed::class.java)
    }

    @Test
    fun whenServiceReturnsInvalidContent_thenRepositoryReturnsFileCouldNotBeParsedError() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.SUMMARY)).thenReturn(
            Single.just("invalid content"),
        )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getSatelliteList()
            .test()
            .assertError(DomainLayerException.AssetCouldNotBeParsed::class.java)
    }

    @Test
    fun whenServiceReturnsEmptyList_thenRepositoryReturnsEmptyList() {
        val assetService = Mockito.mock(AssetService::class.java)
        Mockito.`when`(assetService.readAssetFile(BuildConfig.SUMMARY)).thenReturn(
            Single.just("[]"),
        )
        val assetRepository: AssetRepository = AssetRepositoryImpl(assetService)
        assetRepository.getSatelliteList()
            .test()
            .assertValue(emptyList())
    }
}

package com.ahk.satfinder

import com.ahk.satfinder.core.data.model.SatelliteDetail
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.ahk.satfinder.core.domain.assets.AssetRepository
import com.ahk.satfinder.core.domain.detail.DatabaseRepository
import com.ahk.satfinder.core.domain.detail.GetDetailUseCase
import com.ahk.satfinder.core.domain.detail.GetDetailUseCaseImpl
import com.ahk.satfinder.core.domain.util.DomainLayerException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import org.mockito.Mockito

class GetDetailUseCaseUnitTest {
    @Test
    fun whenDatabaseRepositoryReturnsSatelliteDetail_returnThatSatelliteDetail() {
        val databaseRepository = Mockito.mock(DatabaseRepository::class.java)
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        val id = 1
        val satelliteSummary = SatelliteSummary(
            id = id,
            name = "Satellite-1",
            active = true,
        )
        val detailList = listOf(
            SatelliteDetail(
                id = id,
                name = "",
                costPerLaunch = 12345,
                firstFlight = "2021-01-01",
                height = 12345,
                mass = 12345,
            ),
        )
        Mockito.`when`(databaseRepository.getSatelliteDetailFromDB(id))
            .thenReturn(
                Single.just(detailList),
            )
        val returnDetail = detailList[0]
        returnDetail.name = satelliteSummary.name
        val getDetailUseCase: GetDetailUseCase =
            GetDetailUseCaseImpl(databaseRepository, assetRepository)
        getDetailUseCase.invoke(satelliteSummary)
            .test()
            .assertValue(returnDetail)
    }

    @Test
    fun whenDatabaseRepositoryReturnsEmptyList_returnSatelliteDetailFromAssetRepository() {
        val databaseRepository = Mockito.mock(DatabaseRepository::class.java)
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        val id = 1
        val satelliteSummary = SatelliteSummary(
            id = id,
            name = "Satellite-1",
            active = true,
        )
        val detailList = listOf(
            SatelliteDetail(
                id = id,
                name = "",
                costPerLaunch = 12345,
                firstFlight = "2021-01-01",
                height = 12345,
                mass = 12345,
            ),
            SatelliteDetail(
                id = 2,
                name = "",
                costPerLaunch = 12345,
                firstFlight = "2021-01-01",
                height = 12345,
                mass = 12345,
            ),
            SatelliteDetail(
                id = 3,
                name = "",
                costPerLaunch = 12345,
                firstFlight = "2021-01-01",
                height = 12345,
                mass = 12345,
            ),
        )
        val filteredDetailList = detailList.filter { detail -> detail.id == id }
        val returnDetail = filteredDetailList[0]
        returnDetail.name = satelliteSummary.name
        Mockito.`when`(databaseRepository.getSatelliteDetailFromDB(id))
            .thenReturn(
                Single.just(emptyList()),
            )
        Mockito.`when`(assetRepository.getSatelliteDetailList())
            .thenReturn(
                Single.just(detailList),
            )
        Mockito.`when`(databaseRepository.upsertSatelliteDetail(returnDetail)).thenReturn(
            Completable.complete(),
        )
        println("returnDetail: $returnDetail")
        val getDetailUseCase: GetDetailUseCase =
            GetDetailUseCaseImpl(databaseRepository, assetRepository)
        getDetailUseCase.invoke(satelliteSummary)
            .test()
            .assertValue(returnDetail)
    }

    @Test
    fun whenDatabaseRepositoryAndAssetRepositoryReturnsEmptyList_returnError() {
        val databaseRepository = Mockito.mock(DatabaseRepository::class.java)
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        val id = 1
        val satelliteSummary = SatelliteSummary(
            id = id,
            name = "Satellite-1",
            active = true,
        )
        Mockito.`when`(databaseRepository.getSatelliteDetailFromDB(id))
            .thenReturn(
                Single.just(emptyList()),
            )
        Mockito.`when`(assetRepository.getSatelliteDetailList())
            .thenReturn(
                Single.just(emptyList()),
            )
        val getDetailUseCase: GetDetailUseCase =
            GetDetailUseCaseImpl(databaseRepository, assetRepository)

        getDetailUseCase.invoke(satelliteSummary)
            .test()
            .assertError(DomainLayerException.DetailNotFoundException::class.java)
    }

    @Test
    fun whenDatabaseErrorOccurred_readFromFile() {
        val databaseRepository = Mockito.mock(DatabaseRepository::class.java)
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        val id = 1
        val satelliteSummary = SatelliteSummary(
            id = id,
            name = "Satellite-1",
            active = true,
        )
        val detailList = listOf(
            SatelliteDetail(
                id = id,
                name = "",
                costPerLaunch = 12345,
                firstFlight = "2021-01-01",
                height = 12345,
                mass = 12345,
            ),
            SatelliteDetail(
                id = 2,
                name = "",
                costPerLaunch = 12345,
                firstFlight = "2021-01-01",
                height = 12345,
                mass = 12345,
            ),
            SatelliteDetail(
                id = 3,
                name = "",
                costPerLaunch = 12345,
                firstFlight = "2021-01-01",
                height = 12345,
                mass = 12345,
            ),
        )
        val filteredDetailList = detailList.filter { detail -> detail.id == id }
        Mockito.`when`(databaseRepository.getSatelliteDetailFromDB(id))
            .thenReturn(
                Single.error(DomainLayerException.DatabaseCouldNotBeRead("Database read error")),
            )
        Mockito.`when`(assetRepository.getSatelliteDetailList())
            .thenReturn(
                Single.just(detailList),
            )
        val returnDetail = filteredDetailList[0]
        returnDetail.name = satelliteSummary.name
        Mockito.`when`(databaseRepository.upsertSatelliteDetail(returnDetail)).thenReturn(
            Completable.complete(),
        )
        val getDetailUseCase: GetDetailUseCase =
            GetDetailUseCaseImpl(databaseRepository, assetRepository)
        getDetailUseCase.invoke(satelliteSummary).test().assertValue(returnDetail)
    }

    @Test
    fun whenDatabaseErrorOccurredAndAssetRepositoryReturnsEmptyList_returnError() {
        val databaseRepository = Mockito.mock(DatabaseRepository::class.java)
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        val id = 1
        val satelliteSummary = SatelliteSummary(
            id = id,
            name = "Satellite-1",
            active = true,
        )
        Mockito.`when`(databaseRepository.getSatelliteDetailFromDB(id))
            .thenReturn(
                Single.error(DomainLayerException.DatabaseCouldNotBeRead("Database read error")),
            )
        Mockito.`when`(assetRepository.getSatelliteDetailList())
            .thenReturn(
                Single.just(emptyList()),
            )
        val getDetailUseCase: GetDetailUseCase =
            GetDetailUseCaseImpl(databaseRepository, assetRepository)
        getDetailUseCase.invoke(satelliteSummary)
            .test()
            .assertError(DomainLayerException.DetailNotFoundException::class.java)
    }

    @Test
    fun whenDatabaseErrorOccurredAndAssetRepositoryReturnsError_returnError() {
        val databaseRepository = Mockito.mock(DatabaseRepository::class.java)
        val assetRepository = Mockito.mock(AssetRepository::class.java)
        val id = 1
        val satelliteSummary = SatelliteSummary(
            id = id,
            name = "Satellite-1",
            active = true,
        )
        Mockito.`when`(databaseRepository.getSatelliteDetailFromDB(id))
            .thenReturn(
                Single.error(DomainLayerException.DatabaseCouldNotBeRead("Database read error")),
            )
        Mockito.`when`(assetRepository.getSatelliteDetailList())
            .thenReturn(
                Single.error(DomainLayerException.AssetCouldNotBeParsed("Asset parse error")),
            )
        val getDetailUseCase: GetDetailUseCase =
            GetDetailUseCaseImpl(databaseRepository, assetRepository)
        getDetailUseCase.invoke(satelliteSummary)
            .test()
            .assertError(DomainLayerException.AssetCouldNotBeParsed::class.java)
    }
}

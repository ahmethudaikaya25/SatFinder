package com.ahk.satfinder.core.domain.di

import com.ahk.satfinder.core.data.assets.AssetService
import com.ahk.satfinder.core.data.db.SatelliteDetailDAO
import com.ahk.satfinder.core.domain.assets.*
import com.ahk.satfinder.core.domain.detail.DatabaseRepository
import com.ahk.satfinder.core.domain.detail.DatabaseRepositoryImpl
import com.ahk.satfinder.core.domain.detail.GetDetailUseCase
import com.ahk.satfinder.core.domain.detail.GetDetailUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun provideAssetRepository(assetService: AssetService): AssetRepository =
        AssetRepositoryImpl(assetService)

    @Provides
    fun provideDatabaseRepository(
        satelliteDetailDao: SatelliteDetailDAO,
    ): DatabaseRepository = DatabaseRepositoryImpl(satelliteDetailDao)

    @Provides
    fun provideGetAssetUseCase(assetRepository: AssetRepository): GetSatelliteListUseCase =
        GetSatelliteListUseCaseImpl(assetRepository)

    @Provides
    fun provideFilterAssetUseCase(assetRepository: AssetRepository): FilterAssetsUseCase =
        FilterAssetsUseCaseImpl(assetRepository)

    @Provides
    fun provideGetDetailUseCase(
        databaseRepository: DatabaseRepository,
        assetRepository: AssetRepository,
    ): GetDetailUseCase =
        GetDetailUseCaseImpl(databaseRepository, assetRepository)

    @Provides
    fun provideGetSatellitePositionsUseCase(assetRepository: AssetRepository): GetSatellitePositionsUseCase =
        GetSatellitePositionsUseCaseImpl(assetRepository)
}

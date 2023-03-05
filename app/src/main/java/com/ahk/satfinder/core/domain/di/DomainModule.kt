package com.ahk.satfinder.core.domain.di

import com.ahk.satfinder.core.data.assets.AssetService
import com.ahk.satfinder.core.domain.assets.*
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
    fun provideGetAssetUseCase(assetRepository: AssetRepository): GetAssetsUseCase =
        GetAssetsUseCaseImpl(assetRepository)

    @Provides
    fun provideFilterAssetUseCase(assetRepository: AssetRepository): FilterAssetsUseCase =
        FilterAssetsUseCaseImpl(assetRepository)
}

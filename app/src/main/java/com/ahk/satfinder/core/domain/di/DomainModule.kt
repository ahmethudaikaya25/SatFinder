package com.ahk.satfinder.core.domain.di

import com.ahk.satfinder.core.data.assets.AssetService
import com.ahk.satfinder.core.domain.assets.AssetRepository
import com.ahk.satfinder.core.domain.assets.AssetRepositoryImpl
import com.ahk.satfinder.core.domain.assets.GetAssetListUseCase
import com.ahk.satfinder.core.domain.assets.GetAssetListUseCaseImpl
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
    fun provideGetAssetListUseCase(assetRepository: AssetRepository): GetAssetListUseCase =
        GetAssetListUseCaseImpl(assetRepository)
}

package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.BuildConfig
import com.ahk.satfinder.core.data.assets.AssetService
import com.ahk.satfinder.core.data.model.AssetErrorModels
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single

class AssetRepositoryImpl(
    private val assetService: AssetService,
) : AssetRepository {
    override fun getSatelliteList(): Single<List<SatelliteSummary>> {
        val gson = Gson()
        return assetService.readAssetFile(BuildConfig.SUMMARY).flatMap {
            val type: TypeToken<List<SatelliteSummary>> =
                object : TypeToken<List<SatelliteSummary>>() {}
            val summaries: List<SatelliteSummary>
            try {
                summaries = gson.fromJson(it, type.type)
                return@flatMap Single.just(summaries)
            } catch (e: Exception) {
                return@flatMap Single.error<List<SatelliteSummary>>(
                    AssetErrorModels.AssetCouldNotBeParsed(
                        "Could not parse json",
                    ),
                )
            }
        }
    }
}

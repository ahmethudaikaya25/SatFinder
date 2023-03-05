package com.ahk.satfinder.core.domain.assets

import com.ahk.satfinder.BuildConfig
import com.ahk.satfinder.core.data.assets.AssetService
import com.ahk.satfinder.core.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
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
                    DataLayerException.AssetCouldNotBeParsed(
                        "Could not parse json",
                    ),
                )
            }
        }
    }

    override fun getSatelliteDetailList(): Single<List<SatelliteDetail>> {
        val gson = Gson()
        return assetService.readAssetFile(BuildConfig.DETAIL).flatMap {
            val type: TypeToken<List<SatelliteDetail>> =
                object : TypeToken<List<SatelliteDetail>>() {}
            val details: List<SatelliteDetail>
            try {
                details = gson.fromJson(it, type.type)
                return@flatMap Single.just(details)
            } catch (e: Exception) {
                return@flatMap Single.error<List<SatelliteDetail>>(
                    DataLayerException.AssetCouldNotBeParsed(
                        "Could not parse json",
                    ),
                )
            }
        }
    }

    override fun getPositions(): Observable<List<Positions>> {
        val gson = Gson()
        return Observable.fromSingle(assetService.readAssetFile(BuildConfig.POSITION))
            .flatMap {
                try {
                    val positionFile: PositionFileModel =
                        gson.fromJson(it, PositionFileModel::class.java)
                    return@flatMap Observable.just(positionFile.list)
                } catch (e: Exception) {
                    return@flatMap Observable.error(
                        DataLayerException.AssetCouldNotBeParsed(
                            "Could not parse json",
                        ),
                    )
                }
            }
    }
}

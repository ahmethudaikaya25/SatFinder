package com.ahk.satfinder.core.data.assets

import android.content.res.AssetManager
import com.ahk.satfinder.core.data.model.DataLayerException
import io.reactivex.rxjava3.core.Single

class AssetServiceImpl(val assetManager: AssetManager) : AssetService {
    override fun readAssetFile(fileName: String): Single<String> = try {
        Single.just(assetManager.open(fileName).readBytes().decodeToString())
    } catch (e: Exception) {
        Single.error(DataLayerException.FileCouldNotBeRead("File read error"))
    }
}

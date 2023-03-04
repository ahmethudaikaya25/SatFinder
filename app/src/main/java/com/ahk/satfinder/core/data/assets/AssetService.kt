package com.ahk.satfinder.core.data.assets

import io.reactivex.rxjava3.core.Single

interface AssetService {
    fun readAssetFile(fileName: String): Single<String>
}

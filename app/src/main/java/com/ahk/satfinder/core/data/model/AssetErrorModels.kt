package com.ahk.satfinder.core.data.model

sealed class AssetErrorModels : Exception() {
    data class AssetCouldNotBeParsed(override val message: String) : AssetErrorModels()
    data class AssetCouldNotBeRead(override val message: String) : AssetErrorModels()
}

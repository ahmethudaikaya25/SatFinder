package com.ahk.satfinder.core.data.model

sealed class AssetException : Exception() {
    data class AssetCouldNotBeParsed(override val message: String) : AssetException()
    data class AssetCouldNotBeRead(override val message: String) : AssetException()
}

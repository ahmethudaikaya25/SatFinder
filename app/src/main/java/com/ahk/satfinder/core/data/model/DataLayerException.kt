package com.ahk.satfinder.core.data.model

sealed class DataLayerException : Exception() {
    data class AssetCouldNotBeParsed(override val message: String) : DataLayerException()
    data class FileCouldNotBeRead(override val message: String) : DataLayerException()
    data class DatabaseCouldNotBeRead(override val message: String) : DataLayerException()
    data class DatabaseCouldNotBeWritten(override val message: String) : DataLayerException()
}

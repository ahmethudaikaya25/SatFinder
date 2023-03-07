package com.ahk.satfinder.core.data.model

sealed class DataLayerException : Exception() {
    data class FileCouldNotBeRead(override val message: String) : DataLayerException()
}

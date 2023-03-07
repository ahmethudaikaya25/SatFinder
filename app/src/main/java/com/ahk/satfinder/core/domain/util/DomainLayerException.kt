package com.ahk.satfinder.core.domain.util

sealed class DomainLayerException : Exception() {
    data class AssetCouldNotBeParsed(override val message: String) : DomainLayerException()
    data class DatabaseCouldNotBeRead(override val message: String) : DomainLayerException()
    data class DatabaseCouldNotBeWritten(override val message: String) : DomainLayerException()
    data class ThereIsNoPositionInformation(override val message: String) : DomainLayerException()
    data class DetailNotFoundException(override val message: String) : DomainLayerException()
}

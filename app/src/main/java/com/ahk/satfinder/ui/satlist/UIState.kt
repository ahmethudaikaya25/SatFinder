package com.ahk.satfinder.ui.satlist

import com.ahk.satfinder.core.data.model.SatelliteDetail
import com.ahk.satfinder.core.data.model.SatelliteSummary

sealed class UIState {
    object Idle : UIState()
    object Loading : UIState()
    data class Success(val data: List<SatelliteSummary>) : UIState()
    data class Error(val throwable: Throwable?, val message: String?) : UIState()
    data class NavigateToDetailScreen(val detail: SatelliteDetail) : UIState()
}

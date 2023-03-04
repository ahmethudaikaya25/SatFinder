package com.ahk.satfinder.ui.satlist

import com.ahk.satfinder.core.data.model.SatelliteSummary

sealed class UIState {
    object Idle : UIState()
    object Loading : UIState()
    data class Success(val data: List<SatelliteSummary>) : UIState()
    data class Error(val message: String) : UIState()
}

package com.ahk.satfinder.ui.detail

import com.ahk.satfinder.core.data.model.Position
import com.ahk.satfinder.core.data.model.SatelliteDetail

sealed class UIState {
    object Empty : UIState()
    data class Idle(val satelliteDetail: SatelliteDetail) : UIState()
    data class LoadSatellitePosition(val id: Int) : UIState()
    object Loading : UIState()
    data class Success(val position: Position) : UIState()
    data class Error(val exception: Throwable?, val message: String?) : UIState()
}

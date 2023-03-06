package com.ahk.satfinder.ui.detail

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.ahk.satfinder.R
import com.ahk.satfinder.ui.customview.TitledValue
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("setDetail")
fun setDetail(view: TitledValue, uiState: UIState) {
    when (uiState) {
        is UIState.Idle -> {
            if (view.id == R.id.heightMass) {
                view.setValue("${uiState.satelliteDetail.height}/${uiState.satelliteDetail.mass}")
            }
            if (view.id == R.id.cost) {
                view.setValue(uiState.satelliteDetail.costPerLaunchAsString())
            }
        }
        is UIState.Success -> {
            if (view.id == R.id.lastPosition) {
                view.setValue("(${uiState.position.posX},${uiState.position.posY})")
            }
        }
        else -> {}
    }
}

@BindingAdapter("setDetail")
fun setDetail(view: MaterialTextView, uiState: UIState) {
    when (uiState) {
        is UIState.Idle -> {
            if (view.id == R.id.title) {
                view.text = uiState.satelliteDetail.name
            }
            if (view.id == R.id.launchDate) {
                view.text = uiState.satelliteDetail.firstFlight
            }
        }
        else -> {}
    }
}

@BindingAdapter("setError")
fun errorVisibility(view: MaterialTextView, uiState: UIState) {
    when (uiState) {
        is UIState.Error -> {
            view.visibility = View.VISIBLE
            view.text = uiState.message
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("setLoading")
fun loadingVisibility(view: CircularProgressIndicator, uiState: UIState) {
    when (uiState) {
        is UIState.Loading -> {
            view.visibility = View.VISIBLE
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("setContentVisibility")
fun setContentVisibility(view: ConstraintLayout, uiState: UIState) {
    if (uiState is UIState.Error) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

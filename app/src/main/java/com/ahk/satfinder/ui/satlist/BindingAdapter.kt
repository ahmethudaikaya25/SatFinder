package com.ahk.satfinder.ui.satlist

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.LinearProgressIndicator

@BindingAdapter("setLoadingVisibility")
fun setLoadingVisibility(view: LinearProgressIndicator, uiState: UIState) {
    when (uiState) {
        is UIState.Loading -> {
            view.visibility = View.VISIBLE
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}

package com.ahk.satfinder.ui.satlist

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator

@BindingAdapter("setLoading")
fun setLoadingVisibility(view: CircularProgressIndicator, uiState: UIState) {
    when (uiState) {
        is UIState.Loading -> {
            view.visibility = View.VISIBLE
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("setError")
fun setErrorVisibility(view: View, uiState: UIState) {
    when (uiState) {
        is UIState.Error -> {
            view.visibility = View.VISIBLE
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}
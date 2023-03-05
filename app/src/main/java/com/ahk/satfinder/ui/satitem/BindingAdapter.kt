package com.ahk.satfinder.ui.satitem

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ahk.satfinder.R

@BindingAdapter("setActivityColor")
fun setActivityColor(view: View, isActive: Boolean) {
    if (isActive) {
        view.setBackgroundColor(view.context.getColor(R.color.active_green))
    } else {
        view.setBackgroundColor(view.context.getColor(R.color.inactive_red))
    }
}

@BindingAdapter("setActivityText")
fun setActivityText(view: TextView, isActive: Boolean) {
    if (isActive) {
        view.text = view.context.getString(R.string.active)
    } else {
        view.text = view.context.getString(R.string.inactive)
    }
}

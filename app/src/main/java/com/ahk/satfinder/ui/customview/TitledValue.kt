package com.ahk.satfinder.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.ahk.satfinder.R
import com.ahk.satfinder.databinding.TitledValueBinding

class TitledValue(context: Context, attr: AttributeSet) : ConstraintLayout(context, attr) {

    var binding: TitledValueBinding

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.titled_value,
            this,
            true,
        )
        if (attr != null) {
            val typedArray = context.obtainStyledAttributes(attr, R.styleable.TitledValue)
            val title = typedArray.getString(R.styleable.TitledValue_setTitle)
            val value = typedArray.getString(R.styleable.TitledValue_setValue)
            binding.title.text = title
            binding.value.text = value
            typedArray.recycle()
        }
        binding.root
    }

    fun setValue(value: String) {
        binding.value.text = value
    }
}

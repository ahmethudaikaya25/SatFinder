package com.ahk.satfinder.ui.customview

import android.R
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class RecyclerViewDivider(context: Context) : ItemDecoration() {
    private var divider: Drawable? = null

    init {
        try {
            val a: TypedArray = context.obtainStyledAttributes(ATTRS)
            divider = a.getDrawable(0)
            a.recycle()
        } catch (e: Resources.NotFoundException) {
            // TODO Log or handle as necessary.
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (divider == null) return
        if (parent.getChildAdapterPosition(view) < 1) return
        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.top =
                divider!!.intrinsicHeight
        } else {
            throw IllegalArgumentException("Only usable with vertical lists")
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (divider == null) {
            super.onDrawOver(c!!, parent, state!!)
            return
        }
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val size = divider!!.intrinsicHeight
            val top = (child.top - params.topMargin - size + child.translationY).toInt()
            val bottom = top + size
            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(c)
            if (i == childCount - 1) {
                val newTop =
                    (child.bottom + params.bottomMargin + child.translationY)
                val newBottom = newTop + size
                divider!!.setBounds(left, newTop.toInt(), right, newBottom.toInt())
                divider!!.draw(c)
            }
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        check(parent.layoutManager is LinearLayoutManager) { "Layout manager must be an instance of LinearLayoutManager" }
        return (parent.layoutManager as LinearLayoutManager?)!!.orientation
    }

    companion object {
        private val ATTRS = intArrayOf(R.attr.listDivider)
    }
}

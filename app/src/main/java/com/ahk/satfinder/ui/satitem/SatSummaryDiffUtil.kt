package com.ahk.satfinder.ui.satitem

import androidx.recyclerview.widget.DiffUtil
import com.ahk.satfinder.core.data.model.SatelliteSummary

class SatSummaryDiffUtil(
    private val oldList: List<SatelliteSummary>,
    private val newList: List<SatelliteSummary>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldSatelliteSummary = oldList[oldItemPosition]
        val newSatelliteSummary = newList[newItemPosition]
        return oldSatelliteSummary.id == newSatelliteSummary.id &&
            oldSatelliteSummary.active == newSatelliteSummary.active &&
            oldSatelliteSummary.name == newSatelliteSummary.name
    }
}

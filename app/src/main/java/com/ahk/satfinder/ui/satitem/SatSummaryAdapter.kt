package com.ahk.satfinder.ui.satitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.ahk.satfinder.databinding.ItemSatListBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class SatSummaryAdapter(
    var satSummaries: List<SatelliteSummary>,
) : RecyclerView.Adapter<SatSummaryAdapter.SatSummaryViewHolder>() {

    private val onClick: PublishSubject<SatelliteSummary> = PublishSubject.create()
    val mutableOnClick: Observable<SatelliteSummary> = onClick

    inner class SatSummaryViewHolder(val binding: ItemSatListBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(satSummary: SatelliteSummary) {
            binding.satSummary = satSummary
            binding.root.setOnClickListener(this)
            binding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                satSummaries[position].let {
                    onClick.onNext(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SatSummaryViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            com.ahk.satfinder.R.layout.item_sat_list,
            parent,
            false,
        ) as ItemSatListBinding

        return SatSummaryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return satSummaries.size
    }

    override fun onBindViewHolder(holder: SatSummaryViewHolder, position: Int) {
        holder.bind(satSummaries[position])
    }

    fun setData(newData: List<SatelliteSummary>) {
        val diffUtil = SatSummaryDiffUtil(satSummaries, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        satSummaries = newData
        diffResult.dispatchUpdatesTo(this)
    }
}

package com.ahk.satfinder.ui.satlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ahk.satfinder.core.data.model.SatelliteDetail
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.ahk.satfinder.databinding.FragmentSatListBinding
import com.ahk.satfinder.ui.satitem.SatSummaryAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class SatList : Fragment() {

    private val viewModel: SatListViewModel by viewModels()
    lateinit var binding: FragmentSatListBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSatListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.satList.adapter = SatSummaryAdapter(emptyList()).apply {
            val disposable = mutableOnClick.subscribe(
                viewModel::onListItemClicked,
            )
            compositeDisposable.add(disposable)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(viewLifecycleOwner, ::onStateChange)
        viewModel.loadAssetList()
    }

    private fun onStateChange(uiState: UIState) {
        when (uiState) {
            is UIState.Success -> {
                onSatelliteResultReceived(uiState.data)
            }
            is UIState.NavigateToDetailScreen -> {
                navigateToDetailScreen(uiState.detail)
            }
            else -> {
                // TODO() Add unhandled state or do not anything and log it
                println("Unhandled state: $uiState")
            }
        }
    }

    private fun navigateToDetailScreen(detail: SatelliteDetail) {
        findNavController().navigate(
            SatListDirections.actionSatListToDetail(
                detail,
            ),
        )
    }

    private fun onSatelliteResultReceived(satSummaries: List<SatelliteSummary>) {
        satSummaries.let {
            (binding.satList.adapter as SatSummaryAdapter).setData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}

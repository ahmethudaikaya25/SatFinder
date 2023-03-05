package com.ahk.satfinder.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ahk.satfinder.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Detail : Fragment() {

    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailArgs by navArgs()
    lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(viewLifecycleOwner, ::onStateChange)
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializeView(args.detail)
    }

    private fun onStateChange(uiState: UIState) {
        when (uiState) {
            is UIState.LoadSatellitePosition -> {
                viewModel.loadSatellitePosition(uiState.id)
            }
            else -> {}
        }
    }
}

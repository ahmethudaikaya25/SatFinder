package com.ahk.satfinder.ui.satlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.ahk.satfinder.core.domain.assets.GetAssetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SatListViewModel @Inject constructor(
    private val getAssetListUseCase: GetAssetListUseCase,
) : ViewModel() {

    private val mutableUIState =
        MutableLiveData<UIState>(UIState.Idle)
    val uiState = mutableUIState as LiveData<UIState>

    val compositeDisposable = CompositeDisposable()

    fun onListItemClicked(satelliteSummary: SatelliteSummary) {
        // TODO() Get detailed information and set UIState as set fragment
    }

    fun loadAssetList() {
        mutableUIState.postValue(UIState.Loading)
        val disposable = getAssetListUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .subscribe({ satelliteSum ->
                mutableUIState.postValue(UIState.Success(satelliteSum))
            }, {
                mutableUIState.postValue(UIState.Error("Satellite data getting error"))
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

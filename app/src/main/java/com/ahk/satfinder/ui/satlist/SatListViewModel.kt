package com.ahk.satfinder.ui.satlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.ahk.satfinder.core.domain.assets.FilterAssetsUseCase
import com.ahk.satfinder.core.domain.assets.GetAssetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SatListViewModel @Inject constructor(
    private val getAssetsUseCase: GetAssetsUseCase,
    private val filterAssetsUseCase: FilterAssetsUseCase,
) : ViewModel() {

    private val mutableUIState =
        MutableLiveData<UIState>(UIState.Idle)
    val uiState = mutableUIState as LiveData<UIState>

    val compositeDisposable = CompositeDisposable()

    private val mutableFormState = MutableLiveData(FormState())
    val formState = mutableFormState as LiveData<FormState>

    var searchDisposable: Disposable? = null

    fun onListItemClicked(satelliteSummary: SatelliteSummary) {
        // TODO() Get detailed information and set UIState as set fragment
    }

    fun loadAssetList() {
        mutableUIState.postValue(UIState.Loading)
        val disposable = getAssetsUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .subscribe({ satelliteSum ->
                mutableUIState.postValue(UIState.Success(satelliteSum))
            }, {
                mutableUIState.postValue(
                    UIState.Error(
                        throwable = it,
                        message = "Satellite data getting error",
                    ),
                )
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun searchTextChanged() = searchMovies()

    private fun searchMovies() {
        if (searchDisposable != null) {
            searchDisposable?.dispose()
        }
        mutableUIState.postValue(UIState.Loading)
        formState.value?.search.let {
            searchDisposable =
                filterAssetsUseCase.invoke(it ?: "")
                    .delay(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { result ->
                            mutableUIState.postValue(UIState.Success(result))
                        },
                        { error ->
                            mutableUIState.postValue(
                                UIState.Error(
                                    throwable = error,
                                    message = error.message,
                                ),
                            )
                        },
                    )
        }
    }
}

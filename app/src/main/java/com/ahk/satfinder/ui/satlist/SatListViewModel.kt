package com.ahk.satfinder.ui.satlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahk.satfinder.core.data.model.SatelliteSummary
import com.ahk.satfinder.core.domain.assets.FilterAssetsUseCase
import com.ahk.satfinder.core.domain.assets.GetSatelliteSummariesUseCase
import com.ahk.satfinder.core.domain.detail.GetDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SatListViewModel @Inject constructor(
    private val getSatelliteSummariesUseCase: GetSatelliteSummariesUseCase,
    private val filterAssetsUseCase: FilterAssetsUseCase,
    private val getDetailUseCase: GetDetailUseCase,
) : ViewModel() {

    private val mutableUIState =
        MutableLiveData<UIState>(UIState.Idle)
    val uiState = mutableUIState as LiveData<UIState>

    private val compositeDisposable = CompositeDisposable()

    private val mutableFormState = MutableLiveData(FormState())
    val formState = mutableFormState as LiveData<FormState>

    var searchDisposable: Disposable? = null

    fun onListItemClicked(satelliteSummary: SatelliteSummary) {
        val disposable = getDetailUseCase.invoke(satelliteSummary)
            .subscribeOn(Schedulers.io())
            .subscribe({ satelliteDetail ->
                mutableUIState.postValue(UIState.NavigateToDetailScreen(satelliteDetail))
            }, {
                mutableUIState.postValue(
                    UIState.Error(
                        throwable = it,
                        message = "Satellite detail getting error",
                    ),
                )
            })
        compositeDisposable.add(disposable)
    }

    fun loadAssetList() {
        mutableUIState.postValue(UIState.Loading)
        val disposable = getSatelliteSummariesUseCase.invoke()
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

    fun clearVariables() {
        mutableUIState.postValue(UIState.Idle)
        compositeDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

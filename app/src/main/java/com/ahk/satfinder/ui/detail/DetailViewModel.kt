package com.ahk.satfinder.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahk.satfinder.core.data.model.Position
import com.ahk.satfinder.core.data.model.SatelliteDetail
import com.ahk.satfinder.core.domain.assets.GetSatellitePositionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getSatellitePositionsUseCase: GetSatellitePositionsUseCase,
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mutableUIState = MutableLiveData<UIState>(UIState.Empty)
    val uiState: LiveData<UIState> = mutableUIState

    private val mutablePositionList = MutableLiveData<List<Position>>(emptyList())
    private val mutableSatelliteDetail = MutableLiveData(SatelliteDetail())

    fun initializeView(satelliteDetail: SatelliteDetail) {
        mutableUIState.postValue(UIState.Idle(satelliteDetail))
        mutableSatelliteDetail.postValue(satelliteDetail)
        Observable.timer(100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe {
                schedulePositionLoad()
            }
            .let { compositeDisposable.add(it) }
    }

    private fun schedulePositionLoad() {
        Flowable.interval(3000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe {
                mutableUIState.postValue(
                    UIState.LoadSatellitePosition(
                        mutableSatelliteDetail.value?.id ?: 0,
                    ),
                )
            }
            .let { compositeDisposable.add(it) }
    }

    fun loadSatellitePosition(id: Int) {
        getSatellitePositionsUseCase.invoke(id)
            .flatMap { positionList ->
                val oldPositions = mutablePositionList.value
                val newPositionList = positionList.toMutableList()
                for (position in oldPositions.orEmpty()) {
                    if (positionList.contains(position)) {
                        newPositionList.remove(position)
                    }
                }
                if (newPositionList.isEmpty() && oldPositions != null) {
                    newPositionList.add(oldPositions.last())
                }
                mutablePositionList.postValue(positionList)
                return@flatMap Observable.just(newPositionList)
            }
            .flatMap { positionList ->
                Observable.just(positionList.first())
                    .concatWith(
                        Observable.fromIterable(positionList)
                            .skip(1)
                            .concatMap { position ->
                                Observable.timer(3, TimeUnit.SECONDS)
                                    .map { position }
                            },
                    )
            }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { position ->
                    mutableUIState.postValue(UIState.Success(position))
                },
                { error ->
                    mutableUIState.postValue(UIState.Error(error, error.message))
                },
            ).let { compositeDisposable.add(it) }
    }

    fun clearVariables() {
        mutableUIState.postValue(UIState.Empty)
        compositeDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        clearVariables()
    }
}

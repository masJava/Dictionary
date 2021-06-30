package com.mas.dictionary.view.main

import androidx.lifecycle.LiveData
import com.mas.dictionary.data.AppState
import com.mas.dictionary.datasource.DataSource
import com.mas.dictionary.datasource.DataSourceLocal
import com.mas.dictionary.datasource.DataSourceRemote
import com.mas.dictionary.view.base.BaseViewModel

class MainViewModel(
    private val remoteSource: DataSource<AppState> = DataSourceRemote(),
    private val localSource: DataSource<AppState> = DataSourceLocal(),
) : BaseViewModel<AppState>() {

    override fun getData(word: String, isOnline: Boolean): LiveData<AppState> {
        compositeDisposable.add(
            if (isOnline) {
                remoteSource.getData(word)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe {
                        liveDataForViewToObserve.value = AppState.Loading(null)
                    }
                    .subscribe({
                        liveDataForViewToObserve.value = it
                    }, {
                        liveDataForViewToObserve.value = AppState.Error(it)
                    })
            } else {
                localSource.getData(word)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                    .subscribe({
                        liveDataForViewToObserve.value = it
                    }, {
                        liveDataForViewToObserve.value = AppState.Error(it)
                    })
            }
        )
        return super.getData(word, isOnline)
    }
}

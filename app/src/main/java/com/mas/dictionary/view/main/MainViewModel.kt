package com.mas.dictionary.view.main

import androidx.lifecycle.LiveData
import com.mas.dictionary.data.AppState
import com.mas.dictionary.utils.parseSearchResults
import com.mas.dictionary.view.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private val interactor: MainInteractor) :
    BaseViewModel<AppState>() {

    private var appState: AppState? = null

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    override fun getData(word: String, isOnline: Boolean) {
        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe {
                    liveDataForViewToObserve.value = AppState.Loading(null)
                }
                .subscribe(
                    {
                        appState = parseSearchResults(it)
                        liveDataForViewToObserve.value = appState
                    }, {
                        liveDataForViewToObserve.value = AppState.Error(it)
                    }
                )
        )
    }
}

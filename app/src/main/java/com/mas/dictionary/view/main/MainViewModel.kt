package com.mas.dictionary.view.main

import androidx.lifecycle.LiveData
import com.mas.core.viewmodel.BaseViewModel
import com.mas.dictionary.utils.parseOnlineSearchResults
import com.mas.model.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val interactor: MainInteractor) :
    BaseViewModel<AppState>() {

    private val liveDataForViewToObserve: LiveData<AppState> = mutableLiveData

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    override fun getData(word: String, isOnline: Boolean) {
        mutableLiveData.value = AppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch { this@MainViewModel.startInteractor(word, isOnline) }
    }

    private suspend fun startInteractor(word: String, online: Boolean) =
        withContext(Dispatchers.IO) {
            mutableLiveData.postValue(parseOnlineSearchResults(interactor.getData(word, online)))
        }

    override fun handleError(error: Throwable) {
        mutableLiveData.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        mutableLiveData.postValue(AppState.Success(null))
        super.onCleared()
    }
}

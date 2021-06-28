package com.mas.dictionary.datasource

import com.mas.dictionary.data.AppState
import io.reactivex.Observable

// Для получения внешних данных мы будем использовать Retrofit
class DataSourceRemote(private val remoteProvider: RetrofitImplementation = RetrofitImplementation()) :
    DataSource<AppState> {

    override fun getData(word: String): Observable<AppState> =
        remoteProvider.getData(word).map { AppState.Success(it) }
}


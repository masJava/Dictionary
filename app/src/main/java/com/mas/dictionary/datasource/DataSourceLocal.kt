package com.mas.dictionary.datasource

import com.mas.dictionary.data.AppState
import io.reactivex.Observable

// Для локальных данных используется Room
class DataSourceLocal(private val remoteProvider: RoomDataBaseImplementation = RoomDataBaseImplementation()) :
    DataSource<AppState> {

    override fun getData(word: String): Observable<AppState> =
        remoteProvider.getData(word).map { AppState.Success(it) }
}
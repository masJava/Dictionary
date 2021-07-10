package com.mas.dictionary.datasource

import com.mas.dictionary.data.AppState


interface DataSourceLocal<T> : DataSource<T> {

    suspend fun saveToDB(appState: AppState)
}

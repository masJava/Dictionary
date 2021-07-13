package com.mas.repository.datasource

import com.mas.model.AppState


interface DataSourceLocal<T> : DataSource<T> {

    suspend fun saveToDB(appState: AppState)
}

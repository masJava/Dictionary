package com.mas.dictionary.repository

import com.mas.dictionary.data.AppState

interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: AppState)
}

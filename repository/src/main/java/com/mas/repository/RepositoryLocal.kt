package com.mas.repository

interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: com.mas.model.AppState)
}

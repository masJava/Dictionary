package com.mas.dictionary.repository

import com.mas.dictionary.data.AppState
import com.mas.dictionary.data.DataModel
import com.mas.dictionary.datasource.DataSourceLocal


class RepositoryImplementationLocal(private val dataSource: DataSourceLocal<List<DataModel>>) :
    RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }
}

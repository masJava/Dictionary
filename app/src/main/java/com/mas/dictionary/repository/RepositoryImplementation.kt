package com.mas.dictionary.repository

import com.mas.dictionary.data.DataModel
import com.mas.dictionary.datasource.DataSource

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }
}

package com.mas.repository

import com.mas.model.DataModel
import com.mas.repository.datasource.DataSource


class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }
}

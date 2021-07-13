package com.mas.repository.datasource

import com.mas.dictionary.utils.convertDataModelSuccessToEntity
import com.mas.dictionary.utils.mapHistoryEntityToSearchResult
import com.mas.model.AppState
import com.mas.model.DataModel
import com.mas.repository.room.HistoryDao

class RoomDataBaseImplementation(private val historyDao: HistoryDao) :
    DataSourceLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return mapHistoryEntityToSearchResult(historyDao.all())
    }

    override suspend fun saveToDB(appState: AppState) {
        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }
}

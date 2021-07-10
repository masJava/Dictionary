package com.mas.dictionary.datasource

import com.mas.dictionary.data.AppState
import com.mas.dictionary.data.DataModel
import com.mas.dictionary.room.HistoryDao
import com.mas.dictionary.utils.convertDataModelSuccessToEntity
import com.mas.dictionary.utils.mapHistoryEntityToSearchResult

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

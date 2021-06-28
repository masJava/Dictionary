package com.mas.dictionary.repository

import com.mas.dictionary.data.DataModel
import com.mas.dictionary.datasource.DataSource
import io.reactivex.Observable

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    private val cache = mutableMapOf<String, List<DataModel>>()

    override fun getData(word: String): Observable<List<DataModel>> {
        if (cache.containsKey(word)) return Observable.just(cache[word])
        return dataSource.getData(word)
            .doOnNext { cache[word] = it }
    }

    // Репозиторий возвращает данные, используя dataSource (локальный или
    // внешний)
//    override fun getData(word: String): Observable<List<DataModel>> {
//        return dataSource.getData(word)
//    }
}
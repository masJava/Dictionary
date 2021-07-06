package com.mas.dictionary.datasource

interface DataSource<T> {

    suspend fun getData(word: String): T
}

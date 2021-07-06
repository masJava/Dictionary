package com.mas.dictionary.repository

interface Repository<T> {

    suspend fun getData(word: String): T
}

package com.mas.repository

interface Repository<T> {

    suspend fun getData(word: String): T
}

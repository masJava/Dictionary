package com.mas.dictionary.di

import androidx.room.Room
import com.mas.dictionary.data.DataModel
import com.mas.dictionary.datasource.RetrofitImplementation
import com.mas.dictionary.datasource.RoomDataBaseImplementation
import com.mas.dictionary.repository.Repository
import com.mas.dictionary.repository.RepositoryImplementation
import com.mas.dictionary.repository.RepositoryImplementationLocal
import com.mas.dictionary.repository.RepositoryLocal
import com.mas.dictionary.room.HistoryDataBase
import com.mas.dictionary.view.history.HistoryInteractor
import com.mas.dictionary.view.history.HistoryViewModel
import com.mas.dictionary.view.main.MainInteractor
import com.mas.dictionary.view.main.MainViewModel
import org.koin.dsl.module


val application = module {
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build() }
    single { get<HistoryDataBase>().historyDao() }
    single<Repository<List<DataModel>>> { RepositoryImplementation(RetrofitImplementation()) }
    single<RepositoryLocal<List<DataModel>>> {
        RepositoryImplementationLocal(RoomDataBaseImplementation(get()))
    }
}

val mainScreen = module {
    factory { MainViewModel(get()) }
    factory { MainInteractor(get(), get()) }
}

val historyScreen = module {
    factory { HistoryViewModel(get()) }
    factory { HistoryInteractor(get(), get()) }
}

package com.mas.dictionary.di

import androidx.room.Room
import com.mas.dictionary.view.history.HistoryInteractor
import com.mas.dictionary.view.history.HistoryViewModel
import com.mas.dictionary.view.main.MainInteractor
import com.mas.dictionary.view.main.MainViewModel
import com.mas.model.DataModel
import com.mas.repository.Repository
import com.mas.repository.RepositoryImplementation
import com.mas.repository.RepositoryImplementationLocal
import com.mas.repository.RepositoryLocal
import com.mas.repository.datasource.RetrofitImplementation
import com.mas.repository.datasource.RoomDataBaseImplementation
import com.mas.repository.room.HistoryDataBase
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

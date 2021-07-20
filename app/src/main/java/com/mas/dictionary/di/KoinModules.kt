package com.mas.dictionary.di

import androidx.room.Room
import com.mas.dictionary.view.main.MainActivity
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
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module


fun injectDependencies() = loadModules

private val loadModules by lazy {
    loadKoinModules(listOf(application, mainScreen))
}

val application = module {
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build() }
    single { get<HistoryDataBase>().historyDao() }
    single<Repository<List<DataModel>>> { RepositoryImplementation(RetrofitImplementation()) }
    single<RepositoryLocal<List<DataModel>>> {
        RepositoryImplementationLocal(RoomDataBaseImplementation(get()))
    }
}

val mainScreen = module {
    scope(named<MainActivity>()) {
        scoped { MainInteractor(get(), get()) }
        viewModel { MainViewModel(get()) }
    }
}


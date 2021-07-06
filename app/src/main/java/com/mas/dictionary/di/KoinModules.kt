package com.mas.dictionary.di

import com.mas.dictionary.data.DataModel
import com.mas.dictionary.datasource.RetrofitImplementation
import com.mas.dictionary.datasource.RoomDataBaseImplementation
import com.mas.dictionary.repository.Repository
import com.mas.dictionary.repository.RepositoryImplementation
import com.mas.dictionary.view.main.MainInteractor
import com.mas.dictionary.view.main.MainViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val application = module {
    single<Repository<List<DataModel>>>(named(NAME_REMOTE)) {
        RepositoryImplementation(
            RetrofitImplementation()
        )
    }
    single<Repository<List<DataModel>>>(named(NAME_LOCAL)) {
        RepositoryImplementation(
            RoomDataBaseImplementation()
        )
    }
}

val mainScreen = module {
    factory {
        MainInteractor(
            repositoryRemote = get(named(NAME_REMOTE)),
            repositoryLocal = get(named(NAME_LOCAL))
        )
    }
    factory { MainViewModel(interactor = get()) }
}

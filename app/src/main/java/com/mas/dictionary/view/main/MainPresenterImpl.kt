package com.mas.dictionary.view.main

import com.mas.dictionary.data.AppState
import com.mas.dictionary.datasource.DataSource
import com.mas.dictionary.datasource.DataSourceLocal
import com.mas.dictionary.datasource.DataSourceRemote
import com.mas.dictionary.presenter.Presenter
import com.mas.dictionary.rx.SchedulerProvider
import com.mas.dictionary.view.base.View
import io.reactivex.disposables.CompositeDisposable

class MainPresenterImpl<T : AppState, V : View>(


    // Обратите внимание, что Интерактор мы создаём сразу в конструкторе
//    private val interactor: MainInteractor = MainInteractor(
//        RepositoryImplementation(DataSourceRemote()),
//        RepositoryImplementation(DataSourceLocal())
//    ),
    private val remoteSource: DataSource<AppState> = DataSourceRemote(),
    private val localSource: DataSource<AppState> = DataSourceLocal(),
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    protected val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : Presenter<T, V> {
    // Ссылка на View, никакого контекста
    private var currentView: V? = null

    // Как только появилась View, сохраняем ссылку на неё для дальнейшей работы
    override fun attachView(view: V) {
        if (view != currentView) {
            currentView = view
        }
    }

    // View скоро будет уничтожена: прерываем все загрузки и обнуляем ссылку,
    // чтобы предотвратить утечки памяти и NPE
    override fun detachView(view: V) {
        compositeDisposable.clear()
        if (view == currentView) {
            currentView = null
        }
    }

    // Стандартный код RxJava
    override fun getData(word: String, isOnline: Boolean) {
        compositeDisposable.add(
            if (isOnline) {
                remoteSource.getData(word)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { currentView?.renderData(AppState.Loading(null)) }
                    .subscribe({
                        currentView?.renderData((it))
                    }, {
                        currentView?.renderData(AppState.Error(it))
                    })
            } else {
                localSource.getData(word)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe { currentView?.renderData(AppState.Loading(null)) }
                    .subscribe({
                        currentView?.renderData((it))
                    }, {
                        currentView?.renderData(AppState.Error(it))
                    })
            }
        )
    }
}
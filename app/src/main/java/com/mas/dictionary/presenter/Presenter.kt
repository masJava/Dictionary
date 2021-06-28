package com.mas.dictionary.presenter

import com.mas.dictionary.data.AppState
import com.mas.dictionary.view.base.View

interface Presenter<T : AppState, V : View> {

    fun attachView(view: V)

    fun detachView(view: V)

    // Получение данных с флагом isOnline(из Интернета или нет)
    fun getData(word: String, isOnline: Boolean)

}
package com.mas.dictionary.view.base

import com.mas.dictionary.data.AppState

interface View {
    // View имеет только один метод, в который приходит некое состояние приложения
    fun renderData(appState: AppState)

}

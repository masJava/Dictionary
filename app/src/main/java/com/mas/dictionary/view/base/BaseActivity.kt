package com.mas.dictionary.view.base

import androidx.appcompat.app.AppCompatActivity
import com.mas.dictionary.data.AppState

abstract class BaseActivity<T : AppState> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<T>

    abstract fun renderData(appState: AppState)

}

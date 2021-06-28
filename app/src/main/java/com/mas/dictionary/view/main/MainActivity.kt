package com.mas.dictionary.view.main

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mas.dictionary.R
import com.mas.dictionary.data.AppState
import com.mas.dictionary.data.DataModel
import com.mas.dictionary.databinding.ActivityMainBinding
import com.mas.dictionary.presenter.Presenter
import com.mas.dictionary.view.base.BaseActivity
import com.mas.dictionary.view.base.View
import com.mas.dictionary.view.main.adapter.MainAdapter

class MainActivity : BaseActivity<AppState>() {
    private var adapter: MainAdapter? = null // Адаптер для отображения списка

    // вариантов перевода
    // Обработка нажатия элемента списка
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT).show()
            }
        }

    // Создаём презентер и храним его в базовой Activity
    override fun createPresenter(): Presenter<AppState, View> {
        return MainPresenterImpl()
    }

    private var vb: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ActivityMainBinding.inflate(layoutInflater).also {
            vb = it
        }.root
        setContentView(view)
        vb?.searchFab?.setOnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(object :
                SearchDialogFragment.OnSearchClickListener {
                override fun onClick(searchWord: String) {
                    presenter.getData(searchWord, true)
                }
            })
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    // Переопределяем базовый метод
    override fun renderData(appState: AppState) {
        // В зависимости от состояния модели данных (загрузка, отображение,
        // ошибка) отображаем соответствующий экран
        when (appState) {
            is AppState.Success -> {
                val dataModel = appState.data
                if (dataModel == null || dataModel.isEmpty()) {
                    showErrorScreen(getString(R.string.empty_server_response_on_success))
                } else {
                    showViewSuccess()
                    if (adapter == null) {
                        vb?.mainActivityRecyclerview?.layoutManager =
                            LinearLayoutManager(applicationContext)
                        vb?.mainActivityRecyclerview?.adapter =
                            MainAdapter(onListItemClickListener, dataModel)
                    } else {
                        adapter!!.setData(dataModel)
                    }
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                // Задел на будущее, если понадобится отображать прогресс
                // загрузки
                if (appState.progress != null) {
                    vb?.progressBarHorizontal?.visibility = VISIBLE
                    vb?.progressBarRound?.visibility = GONE
                    vb?.progressBarHorizontal?.progress = appState.progress
                } else {
                    vb?.progressBarHorizontal?.visibility = GONE
                    vb?.progressBarRound?.visibility = VISIBLE
                }
            }
            is AppState.Error -> {
                showErrorScreen(appState.error.message)
            }
        }
    }

    private fun showErrorScreen(error: String?) {
        showViewError()
        vb?.errorTextview?.text = error ?: getString(R.string.undefined_error)
        vb?.reloadButton?.setOnClickListener {
            presenter.getData("hi", true)
        }
    }

    private fun showViewSuccess() {
        vb?.successLinearLayout?.visibility = VISIBLE
        vb?.loadingFrameLayout?.visibility = GONE
        vb?.errorLinearLayout?.visibility = GONE
    }

    private fun showViewLoading() {
        vb?.successLinearLayout?.visibility = GONE
        vb?.loadingFrameLayout?.visibility = VISIBLE
        vb?.errorLinearLayout?.visibility = GONE
    }

    private fun showViewError() {
        vb?.successLinearLayout?.visibility = GONE
        vb?.loadingFrameLayout?.visibility = GONE
        vb?.errorLinearLayout?.visibility = VISIBLE
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }

}
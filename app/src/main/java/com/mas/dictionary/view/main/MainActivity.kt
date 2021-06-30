package com.mas.dictionary.view.main

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mas.dictionary.R
import com.mas.dictionary.data.AppState
import com.mas.dictionary.data.DataModel
import com.mas.dictionary.databinding.ActivityMainBinding
import com.mas.dictionary.view.base.BaseActivity
import com.mas.dictionary.view.main.adapter.MainAdapter

class MainActivity : BaseActivity<AppState>() {
    override val viewModel: MainViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
    }

    private val observer = Observer<AppState> { renderData(it) }

    private var adapter: MainAdapter? = null

    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT).show()
            }
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
                    viewModel.getData(searchWord, true).observe(this@MainActivity, observer)
                }
            })
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }
    }

    override fun renderData(appState: AppState) {
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
            viewModel.getData("hi", true).observe(this, observer)
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
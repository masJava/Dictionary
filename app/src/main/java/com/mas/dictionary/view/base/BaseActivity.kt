package com.mas.dictionary.view.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mas.dictionary.R
import com.mas.dictionary.data.AppState
import com.mas.dictionary.data.DataModel
import com.mas.dictionary.databinding.LoadingLayoutBinding
import com.mas.dictionary.utils.network.isOnline
import com.mas.dictionary.utils.ui.AlertDialogFragment
import com.mas.dictionary.viewmodel.BaseViewModel
import com.mas.dictionary.viewmodel.Interactor

abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity() {

    abstract val model: BaseViewModel<T>

    protected var isNetworkAvailable: Boolean = false
    private var vb: LoadingLayoutBinding? = null


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        isNetworkAvailable = isOnline(applicationContext)
        val view = LoadingLayoutBinding.inflate(layoutInflater).also {
            vb = it
        }.root
    }

    override fun onResume() {
        super.onResume()
        isNetworkAvailable = isOnline(applicationContext)
        if (!isNetworkAvailable && isDialogNull()) {
            showNoInternetConnectionDialog()
        }
    }

    protected fun showNoInternetConnectionDialog() {
        showAlertDialog(
            "No internet connection",
            "You can not use app without internet connection. Please, check internet connection."
        )
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        AlertDialogFragment.newInstance(title, message)
            .show(supportFragmentManager, DIALOG_FRAGMENT_TAG)
    }

    private fun isDialogNull(): Boolean {
        return supportFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null
    }

    protected fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                val data = appState.data
                if (data.isNullOrEmpty()) {
                    showAlertDialog(
                        "Sorry",
                        getString(R.string.empty_server_response_on_success)
                    )
                } else {
                    setDataToAdapter(data)
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    vb?.progressBarHorizontal?.visibility = View.VISIBLE
                    vb?.progressBarRound?.visibility = View.GONE
                    vb?.progressBarHorizontal?.progress = appState.progress
                } else {
                    vb?.progressBarHorizontal?.visibility = View.GONE
                    vb?.progressBarRound?.visibility = View.VISIBLE
                }
            }
            is AppState.Error -> {
                showViewWorking()
                showAlertDialog("Error", appState.error.message)
            }
        }
    }

    private fun showViewWorking() {
        vb?.loadingFrameLayout?.visibility = View.GONE
    }

    private fun showViewLoading() {
        vb?.loadingFrameLayout?.visibility = View.VISIBLE
    }

    companion object {
        private const val DIALOG_FRAGMENT_TAG = "74a54328-5d62-46bf-ab6b-cbf5d8c79522"
    }

    abstract fun setDataToAdapter(data: List<DataModel>)

}

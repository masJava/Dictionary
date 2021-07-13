package com.mas.core

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mas.core.databinding.LoadingLayoutBinding
import com.mas.core.viewmodel.BaseViewModel
import com.mas.core.viewmodel.Interactor
import com.mas.model.AppState
import com.mas.model.DataModel
import com.mas.utils.network.isOnline
import com.mas.utils.ui.AlertDialogFragment

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
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
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
                        getString(R.string.dialog_tittle_sorry),
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
                    vb?.progressBarHorizontal?.progress = appState.progress!!
                } else {
                    vb?.progressBarHorizontal?.visibility = View.GONE
                    vb?.progressBarRound?.visibility = View.VISIBLE
                }
            }
            is AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
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

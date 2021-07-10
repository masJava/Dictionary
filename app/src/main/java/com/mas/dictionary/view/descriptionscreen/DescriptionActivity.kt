package com.mas.dictionary.view.descriptionscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mas.dictionary.R
import com.mas.dictionary.databinding.ActivityDescriptionBinding
import com.mas.dictionary.utils.network.isOnline
import com.mas.dictionary.utils.ui.AlertDialogFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class DescriptionActivity : AppCompatActivity() {
    private var vb: ActivityDescriptionBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ActivityDescriptionBinding.inflate(layoutInflater).also {
            vb = it
        }.root

        setContentView(view)

        setActionbarHomeButtonAsUp()
        vb?.descriptionScreenSwipeRefreshLayout?.setOnRefreshListener { startLoadingOrShowError() }
        setData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setActionbarHomeButtonAsUp() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setData() {
        val bundle = intent.extras
        vb?.descriptionHeader?.text = bundle?.getString(WORD_EXTRA)
        vb?.descriptionTextview?.text = bundle?.getString(DESCRIPTION_EXTRA)
        val imageLink = bundle?.getString(URL_EXTRA)
        if (imageLink.isNullOrBlank()) {
            stopRefreshAnimationIfNeeded()
        } else {
            vb?.descriptionImageview?.let { usePicassoToLoadPhoto(it, imageLink) }
            //useGlideToLoadPhoto(description_imageview, imageLink)
        }
    }

    private fun startLoadingOrShowError() {
        if (isOnline(applicationContext)) {
            setData()
        } else {
            AlertDialogFragment.newInstance(
                "No internet connection",
                "Please, check internet connection."
            ).show(
                supportFragmentManager,
                DIALOG_FRAGMENT_TAG
            )
            stopRefreshAnimationIfNeeded()
        }
    }

    private fun stopRefreshAnimationIfNeeded() {
        if (vb?.descriptionScreenSwipeRefreshLayout?.isRefreshing == true) {
            vb?.descriptionScreenSwipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun usePicassoToLoadPhoto(imageView: ImageView, imageLink: String) {
        Picasso.with(applicationContext).load("https:$imageLink")
            .placeholder(R.drawable.ic_no_photo_vector).fit().centerCrop()
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    stopRefreshAnimationIfNeeded()
                }

                override fun onError() {
                    stopRefreshAnimationIfNeeded()
                    imageView.setImageResource(R.drawable.ic_baseline_error_24)
                }
            })
    }

//    private fun useGlideToLoadPhoto(imageView: ImageView, imageLink: String) {
//        Glide.with(imageView)
//            .load("https:$imageLink")
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    stopRefreshAnimationIfNeeded()
//                    imageView.setImageResource(R.drawable.ic_load_error_vector)
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    stopRefreshAnimationIfNeeded()
//                    return false
//                }
//            })
//            .apply(
//                RequestOptions()
//                    .placeholder(R.drawable.ic_no_photo_vector)
//                    .centerCrop()
//            )
//            .into(imageView)
//    }

    companion object {

        private const val DIALOG_FRAGMENT_TAG = "8c7dff51-9769-4f6d-bbee-a3896085e76e"

        private const val WORD_EXTRA = "f76a288a-5dcc-43f1-ba89-7fe1d53f63b0"
        private const val DESCRIPTION_EXTRA = "0eeb92aa-520b-4fd1-bb4b-027fbf963d9a"
        private const val URL_EXTRA = "6e4b154d-e01f-4953-a404-639fb3bf7281"

        fun getIntent(
            context: Context,
            word: String,
            description: String,
            url: String?
        ): Intent = Intent(context, DescriptionActivity::class.java).apply {
            putExtra(WORD_EXTRA, word)
            putExtra(DESCRIPTION_EXTRA, description)
            putExtra(URL_EXTRA, url)
        }
    }
}

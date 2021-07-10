package com.mas.dictionary.application

import android.app.Application
import com.mas.dictionary.di.application
import com.mas.dictionary.di.historyScreen
import com.mas.dictionary.di.mainScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TranslatorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(listOf(application, mainScreen, historyScreen))
        }
    }
}

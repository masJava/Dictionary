package com.mas.dictionary.application

import android.app.Application
import com.mas.dictionary.di.application
import com.mas.dictionary.di.mainScreen
import org.koin.core.context.startKoin

class TranslatorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(application, mainScreen))
        }
    }
}

package com.arch.search

import android.app.Application
import com.arch.search.di.searchModules
import com.arch.search.util.installTls12
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OnApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        applicationContext.installTls12()
        startKoin {
            androidContext(this@OnApplication)
            modules(searchModules)
        }

        if(BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this)
        }
    }
}

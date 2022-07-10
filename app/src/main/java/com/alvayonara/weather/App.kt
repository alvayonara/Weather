package com.alvayonara.weather

import android.app.Application
import com.alvayonara.core.di.CoreComponent
import com.alvayonara.core.di.DaggerCoreComponent
import com.alvayonara.weather.di.AppComponent
import com.alvayonara.weather.di.DaggerAppComponent
import timber.log.Timber

class App: Application() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant()
        }
    }
}
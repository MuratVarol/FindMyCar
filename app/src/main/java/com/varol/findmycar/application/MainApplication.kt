package com.varol.findmycar.application

import android.app.Application
import com.varol.findmycar.di.*
import org.koin.android.ext.android.startKoin


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin(
            this,
            listOf(
                appModule,
                networkModule,
                repositoryModule,
                dataSourceModule,
                useCaseModule,
                viewModelModule
            )
        )
    }
}

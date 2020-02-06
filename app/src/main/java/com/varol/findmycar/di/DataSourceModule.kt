package com.varol.findmycar.di

import com.varol.findmycar.data.remote.datasoruce.CarsDataSource
import org.koin.dsl.module.Module
import org.koin.dsl.module.module


val dataSourceModule: Module = module {
    single { CarsDataSource(get()) }
}

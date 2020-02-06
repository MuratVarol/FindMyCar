package com.varol.findmycar.di

import com.varol.findmycar.data.remote.repository.CarsRepository
import org.koin.dsl.module.Module
import org.koin.dsl.module.module


val repositoryModule: Module = module {
    single { CarsRepository(get()) }
}

package com.varol.findmycar.di

import com.varol.findmycar.domain.GetAllCarsUseCase
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val useCaseModule: Module = module {
    single { GetAllCarsUseCase(get()) }

}
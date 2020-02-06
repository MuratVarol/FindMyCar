package com.varol.findmycar.di

import com.varol.findmycar.screen.car.CarsViewModel
import com.varol.findmycar.screen.car.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val viewModelModule: Module = module {
    viewModel { CarsViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
}
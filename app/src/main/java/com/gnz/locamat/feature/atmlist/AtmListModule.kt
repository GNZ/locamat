package com.gnz.locamat.feature.atmlist

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val atmListModule = module {
    viewModel { ATMListViewModel(get(), get()) }
}
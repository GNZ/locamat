package com.gnz.locamat.feature.atmlist

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val atmListModule = module {
    viewModel { ATMListViewModel(get(), get(), get(), get()) }
}
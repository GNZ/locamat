package com.gnz.locamat.feature.map

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.gnz.locamat.data.LocATM
import com.gnz.locamat.repository.local.LocalRepository
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


class MapViewModel(private val localRepository: LocalRepository) : ViewModel() {


    val atmLiveData: LiveData<List<LocATM>> = localRepository.observeAll()
}

val mapModule = module {
    viewModel { MapViewModel(get()) }
}
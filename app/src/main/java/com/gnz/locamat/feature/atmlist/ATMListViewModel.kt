package com.gnz.locamat.feature.atmlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gnz.locamat.data.LocATM
import com.gnz.locamat.data.toLocATM
import com.gnz.locamat.repository.local.LocalRepository
import com.gnz.locamat.repository.remote.RemoteRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers


class ATMListViewModel(private val remoteRepository: RemoteRepository,
                       private val localRepository: LocalRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

    val atmListLiveData by lazy {
        LivePagedListBuilder<Int, LocATM>(localRepository.observeAllPaged(), config).build()
    }

    init {
        fetchAndStoreATMs()
    }

    private fun fetchAndStoreATMs() {
        compositeDisposable += remoteRepository.getAtms()
                .subscribeOn(Schedulers.io())
                .flattenAsFlowable { it }
                .map { ATM -> ATM.toLocATM() }
                .subscribe { localRepository::insert }
    }

    fun onClick(atm: LocATM){

    }

    companion object {
        const val PAGE_SIZE = 30
    }
}
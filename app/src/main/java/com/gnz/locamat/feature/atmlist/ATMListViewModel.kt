package com.gnz.locamat.feature.atmlist

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gnz.locamat.common.DistanceUtil
import com.gnz.locamat.data.DisATM
import com.gnz.locamat.data.LocATM
import com.gnz.locamat.data.getLocation
import com.gnz.locamat.data.toLocATM
import com.gnz.locamat.repository.local.LocalRepository
import com.gnz.locamat.repository.remote.RemoteRepository
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

    private val locationLiveData by lazy {
        MutableLiveData<Location>()
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

    fun observeAtms() = Transformations.switchMap(locationLiveData) { location ->
        LivePagedListBuilder<Int, DisATM>(
                localRepository.observeAllPaged()
                        .map { locAtm ->
                            DisATM(locAtm.id!!,
                                    locAtm.name,
                                    locAtm.formatted,
                                    DistanceUtil.calculateDistance(locAtm.getLocation(), location))
                        }, config)
                .build()
    }

    fun updateLocation(location: Location){
        locationLiveData.postValue(location)
    }

    fun onClick(atm: LocATM) {

    }

    companion object {
        const val PAGE_SIZE = 30
    }
}
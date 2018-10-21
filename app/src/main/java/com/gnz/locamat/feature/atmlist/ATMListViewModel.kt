package com.gnz.locamat.feature.atmlist

import android.location.Location
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.musichin.reactivelivedata.switchLatest
import com.github.musichin.reactivelivedata.switchMap
import com.gnz.locamat.common.DistanceUtil
import com.gnz.locamat.data.*
import com.gnz.locamat.repository.local.LocalRepository
import com.gnz.locamat.repository.remote.RemoteRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers


class ATMListViewModel(private val remoteRepository: RemoteRepository,
                       private val localRepository: LocalRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var location = Location("Me now").apply {
        altitude = 53.853847
        longitude = 18.629034
    }

    private val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

//    private val mediatorLiveData = MediatorLiveData<PagedList<DisATM>>()

    private val switchMap = MutableLiveData<LiveData<PagedList<DisATM>>>()

    private val stateLiveData by lazy {
        MutableLiveData<ResourceState>()
    }

    init {
        stateLiveData.postValue(EmptyState)
        fetchAndStoreATMs()
        switchMap.value = mapPagedList(null)
        //updateLocation(location)
//        mediatorLiveData.addSource(observeDatabase()) { list ->
//            handleResponse(list)
//        }
    }

    // TODO this will be use later for the search
//    private fun handleResponse(list: PagedList<DisATM>) {
//        if (list.isNotEmpty()) {
//            mediatorLiveData.value = list
//            stateLiveData.postValue(PopulateState)
//        } else {
//            stateLiveData.postValue(EmptyState)
//        }
//    }

    private fun fetchAndStoreATMs() {
        compositeDisposable += remoteRepository.getAtms()
                .subscribeOn(Schedulers.io())
                .map { atmList -> atmList.map { it.toLocATM() } }
                .subscribe(localRepository::insertAll)
    }

    fun observeResultState(): LiveData<ResourceState> = stateLiveData

    fun observeAtms():LiveData<PagedList<DisATM>> = switchMap.switchLatest()

    private fun mapPagedList(location: Location?): LiveData<PagedList<DisATM>> = LivePagedListBuilder<Int, DisATM>(
            localRepository.observeAllPaged()
                    .map { locAtm ->
                        val distanceResult = if (location == null) {
                            NONE
                        } else {
                            Result(DistanceUtil.calculateDistance(locAtm.getLocation(), location))
                        }

                        DisATM(locAtm.id!!,
                                locAtm.name,
                                locAtm.formatted,
                                distanceResult
                        )
                    },
            config).build()

    fun updateLocation(location: Location) {
        switchMap.value = mapPagedList(location)
    }

    fun onClick(atm: DisATM) {

    }

    companion object {
        const val PAGE_SIZE = 30
    }
}
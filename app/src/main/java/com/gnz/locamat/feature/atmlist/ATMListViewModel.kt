package com.gnz.locamat.feature.atmlist

import android.location.Location
import androidx.lifecycle.*
import androidx.paging.DataSource
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

    private var location = Location("Me now").apply {
        altitude = 53.853847
        longitude = 18.629034
    }

    private val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

    private val mediatorLiveData = MediatorLiveData<PagedList<DisATM>>()

    private val locationLiveData by lazy {
        MutableLiveData<Location>()
    }

    init {
        fetchAndStoreATMs()
        updateLocation(location)
        mediatorLiveData.addSource(observeLocation()) { list ->
            if (list.isNotEmpty()) {
                mediatorLiveData.value = list
            }
        }
        mediatorLiveData.addSource(observeDatabase()) { list ->
            if (list.isNotEmpty()) {
                mediatorLiveData.value = list
            }
        }
    }

    private fun fetchAndStoreATMs() {
        compositeDisposable += remoteRepository.getAtms()
                .subscribeOn(Schedulers.io())
                .map { atmList -> atmList.map { it.toLocATM() } }
                .subscribe(localRepository::insertAll)
    }

    fun observeAtms(): LiveData<PagedList<DisATM>> = mediatorLiveData

    private fun observeLocation() = Transformations.switchMap(locationLiveData) { location ->
        mapPagedList(location)
    }

    private fun observeDatabase(): LiveData<PagedList<DisATM>> = mapPagedList(location)

    private fun mapPagedList(location: Location): LiveData<PagedList<DisATM>> = LivePagedListBuilder<Int, DisATM>(
            localRepository.observeAllPaged()
                    .map { locAtm ->
                        DisATM(locAtm.id!!,
                                locAtm.name,
                                locAtm.formatted,
                                DistanceUtil.calculateDistance(locAtm.getLocation(), location))
                    },
            config).build()

    fun updateLocation(location: Location) {
        this.location = location
        locationLiveData.postValue(location)
    }

    fun onClick(atm: DisATM) {

    }

    companion object {
        const val PAGE_SIZE = 30
    }
}
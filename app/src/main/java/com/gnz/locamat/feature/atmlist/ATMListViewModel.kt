package com.gnz.locamat.feature.atmlist

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.location.Location
import com.gnz.locamat.common.DistanceUtil
import com.gnz.locamat.common.switchLatest
import com.gnz.locamat.data.*
import com.gnz.locamat.repository.local.LocalRepository
import com.gnz.locamat.repository.location.LocationPermissionCheck
import com.gnz.locamat.repository.location.LocationRepository
import com.gnz.locamat.repository.remote.RemoteRepository
import com.google.android.gms.location.LocationRequest
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers


class ATMListViewModel(private val remoteRepository: RemoteRepository,
                       private val localRepository: LocalRepository,
                       private val locationRepository: LocationRepository,
                       private val locationPermissionCheck: LocationPermissionCheck) : ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private var locationDisposable: Disposable? = null
    private var locationRequest: LocationRequest? = null

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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        locationRequest?.let { startListeningLocation(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        locationDisposable?.dispose()
    }

    fun observeResultState(): LiveData<ResourceState> = stateLiveData

    fun observeAtms(): LiveData<PagedList<DisATM>> = switchMap.switchLatest()

    private fun mapPagedList(location: Location?): LiveData<PagedList<DisATM>> = LivePagedListBuilder<Int, DisATM>(
            localRepository.observeAllPaged()
                    .map { locAtm: LocATM ->
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

    fun startListeningLocation(locationRequest: LocationRequest) {
        locationDisposable = locationPermissionCheck.checkLocationPermission(locationRequest)
                .subscribeOn(Schedulers.io())
                .flatMapObservable { granted ->
                    if (granted) {
                        locationRepository.observeLocation(locationRequest)
                                .map { LocationPermission.LocationGranted(it) }
                    } else {
                        Observable.just(LocationPermission.NotGranted)
                    }
                }
                .subscribe { locationPermission ->
                    when (locationPermission) {
                        LocationPermission.NotGranted -> showNotGranted()
                        is LocationPermission.LocationGranted ->
                            switchMap.value = mapPagedList(location)
                    }
                }
    }

    fun showNotGranted() {

    }

    fun onClick(atm: DisATM) {

    }

    internal sealed class LocationPermission {
        object NotGranted : LocationPermission()
        data class LocationGranted(val location: Location) : LocationPermission()
    }

    companion object {
        const val PAGE_SIZE = 30
    }
}
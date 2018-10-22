package com.gnz.locamat.feature.atmlist

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.location.Location
import com.gnz.locamat.data.*
import com.gnz.locamat.repository.local.LocalRepository
import com.gnz.locamat.repository.location.LocationPermissionCheck
import com.gnz.locamat.repository.location.LocationRepository
import com.gnz.locamat.repository.remote.RemoteRepository
import com.google.android.gms.location.LocationRequest
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


class ATMListViewModel(private val remoteRepository: RemoteRepository,
                       private val localRepository: LocalRepository,
                       private val locationRepository: LocationRepository,
                       private val locationPermissionCheck: LocationPermissionCheck) : ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private var locationDisposable: CompositeDisposable = CompositeDisposable()
    private var locationRequest: LocationRequest? = null

    private val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

//    private val mediatorLiveData = MediatorLiveData<PagedList<DisATM>>()

    private val atmList = LivePagedListBuilder<Int, LocATM>(localRepository.observeAllPaged(), config).build()

    private val locationLiveData = MutableLiveData<Location>()

    private val stateLiveData by lazy {
        MutableLiveData<ResourceState>()
    }

    init {
        stateLiveData.postValue(EmptyState)
        fetchAndStoreATMs()
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
        locationDisposable.dispose()
    }

    fun observeResultState(): LiveData<ResourceState> = stateLiveData

    fun observeAtms(): LiveData<PagedList<LocATM>> = atmList

    fun observeLocation(): LiveData<Location> = locationLiveData

    fun startListeningLocation(locationRequest: LocationRequest) {
        locationDisposable += locationPermissionCheck.checkLocationPermission(locationRequest)
                .flatMapObservable { granted ->
                    if (granted) {
                        locationRepository.observeLocation(locationRequest)
                                .subscribeOn(Schedulers.io())
                                .map { LocationPermission.LocationGranted(it) }
                    } else {
                        Observable.just(LocationPermission.NotGranted)
                    }
                }
                .subscribe({ locationPermission ->
                    when (locationPermission) {
                        LocationPermission.NotGranted -> showNotGranted()
                        is LocationPermission.LocationGranted ->
                            locationLiveData.postValue(locationPermission.location)
                    }
                }, {throwable ->
                    Timber.e(throwable, "Couldn't get the location")
                    stateLiveData.postValue(LocationError)
                })
    }

    fun showNotGranted() {
        stateLiveData.postValue(NoLocationGranted)
    }

    fun onClick(atm: LocATM) {

    }

    internal sealed class LocationPermission {
        object NotGranted : LocationPermission()
        data class LocationGranted(val location: Location) : LocationPermission()
    }

    companion object {
        const val PAGE_SIZE = 30
    }
}
package com.gnz.locamat.repository.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import io.reactivex.Maybe
import io.reactivex.Observable

@SuppressLint("MissingPermission")
class LocationRepositoryImpl(private val rxLocation: RxLocation) : LocationRepository {

    override fun observeLocation(locationRequest: LocationRequest): Observable<Location> = rxLocation.location()
            .updates(locationRequest)

    override fun getLocation(): Maybe<Location> = rxLocation.location().lastLocation()
}
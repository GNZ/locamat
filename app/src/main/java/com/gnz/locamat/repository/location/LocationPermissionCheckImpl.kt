package com.gnz.locamat.repository.location

import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import io.reactivex.Single


class LocationPermissionCheckImpl(private val rxLocation: RxLocation) : LocationPermissionCheck {

    override fun checkLocationPermission(locationRequest: LocationRequest): Single<Boolean> =
            rxLocation
                    .settings()
                    .checkAndHandleResolution(locationRequest)
}
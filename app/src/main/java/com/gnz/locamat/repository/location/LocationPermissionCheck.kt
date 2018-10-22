package com.gnz.locamat.repository.location

import com.google.android.gms.location.LocationRequest
import io.reactivex.Single


interface LocationPermissionCheck {

    fun checkLocationPermission(locationRequest: LocationRequest): Single<Boolean>
}
package com.gnz.locamat.repository.location

import android.location.Location
import com.google.android.gms.location.LocationRequest
import io.reactivex.Maybe
import io.reactivex.Observable


interface LocationRepository {

    fun observeLocation(locationRequest: LocationRequest): Observable<Location>

    fun getLocation(): Maybe<Location>
}
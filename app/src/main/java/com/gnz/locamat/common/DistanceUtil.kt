package com.gnz.locamat.common

import android.location.Location


object DistanceUtil {

    fun calculateDistance(location1: Location, location2: Location): Float =
            location1.distanceTo(location2)

    fun formatDistance(distance: Float): FormattedDistance =
            when (distance) {
                in 0 until 1 -> FormattedDistance(0, DistanceUnit.M)
                in 1 until KILOMETER -> FormattedDistance(distance.toInt(), DistanceUnit.M)
                else -> FormattedDistance(distance.toInt() / KILOMETER, DistanceUnit.KM)
            }

    data class FormattedDistance(val distance: Int, val distanceUnit: DistanceUnit)

    enum class DistanceUnit {
        KM, M
    }

    private const val KILOMETER = 1000
}
package com.gnz.locamat.data


// Distance is in meters
data class DisATM(val id: Long, val name: String, val address: String, val distanceResult: DistanceResult)

sealed class DistanceResult

object NONE : DistanceResult()

data class Result(val distance: Float) : DistanceResult()
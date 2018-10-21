package com.gnz.locamat.data

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TABLE_NAME)
data class LocATM(
        @PrimaryKey(autoGenerate = true) val id: Long? = null,
        val name: String,
        val city: String,
        val zip: String,
        val formatted: String,
        val latitude: Double,
        val longitude: Double)

const val TABLE_NAME = "atm"

fun ATM.toLocATM(): LocATM = LocATM(name = name,
        city = address.city,
        zip = address.zip,
        formatted = address.formatted,
        latitude = latitude,
        longitude = longitude)

fun LocATM.getLocation() = Location(name).apply {
    latitude = this@getLocation.latitude
    longitude = this@getLocation.longitude
}
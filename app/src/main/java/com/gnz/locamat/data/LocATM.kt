package com.gnz.locamat.data

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
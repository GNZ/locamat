package com.gnz.locamat.repository.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.gnz.locamat.data.LocATM

@Database(entities = [LocATM::class], version = 1)
abstract class ATMDataBase : RoomDatabase() {

    abstract fun atmDao(): ATMDao
}

const val DATABASE_NAME = "ATM_DATABASE"
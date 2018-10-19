package com.gnz.locamat.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gnz.locamat.data.LocATM

@Database(entities = [LocATM::class], version = 1)
abstract class ATMDataBase : RoomDatabase() {

    abstract fun atmDao(): ATMDao
}

const val DATABASE_NAME = "ATM_DATABASE"
package com.gnz.locamat.repository.local

import android.arch.persistence.room.Room
import android.content.Context
import org.koin.dsl.module.module


val localRepositoryModule = module {
    single { getDatabase(get()) }
    single { getAtmDao(get()) }
    single { getLocalRepository(get()) }
}

fun getDatabase(context: Context): ATMDataBase = Room
        .databaseBuilder(
                context.applicationContext,
                ATMDataBase::class.java, DATABASE_NAME)
        .build()

fun getAtmDao(database: ATMDataBase): ATMDao = database.atmDao()

fun getLocalRepository(atmDao: ATMDao): LocalRepository = RoomLocalRepository(atmDao)
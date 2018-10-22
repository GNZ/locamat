package com.gnz.locamat.repository.location

import com.patloew.rxlocation.RxLocation
import org.koin.dsl.module.module


val locationModule = module {
    single { RxLocation(get()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    single<LocationPermissionCheck> { LocationPermissionCheckImpl(get()) }
}
package com.gnz.locamat.app

import android.app.Application
import com.gnz.locamat.feature.atmlist.atmListModule
import com.gnz.locamat.feature.map.mapModule
import com.gnz.locamat.repository.local.localRepositoryModule
import com.gnz.locamat.repository.location.locationModule
import com.gnz.locamat.repository.remote.networkModule
import com.gnz.locamat.repository.remote.remoteRepositoryModule
import org.koin.android.ext.android.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(networkModule,
                remoteRepositoryModule,
                localRepositoryModule,
                atmListModule,
                locationModule,
                mapModule))
    }
}
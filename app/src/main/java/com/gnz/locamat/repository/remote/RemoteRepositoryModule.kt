package com.gnz.locamat.repository.remote

import org.koin.dsl.module.module
import retrofit2.Retrofit


val remoteRepositoryModule = module {
    single { createApi(get()) }
    single { createRemoteRepository(get()) }
}

fun createApi(retrofit: Retrofit): ATMApi = retrofit.create(ATMApi::class.java)

fun createRemoteRepository(atmApi: ATMApi): RemoteRepository = RemoteRepositoryImpl(atmApi)
package com.gnz.locamat.repository.remote

import com.gnz.locamat.data.ATM
import io.reactivex.Single


class RemoteRepositoryImpl(private val atmApi: ATMApi) : RemoteRepository {

    override fun getAtms(): Single<List<ATM>> = atmApi.getAtms()
}
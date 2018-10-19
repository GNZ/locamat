package com.gnz.locamat.repository.remote

import com.gnz.locamat.data.ATM
import io.reactivex.Single


interface RemoteRepository {

    fun getAtms(): Single<List<ATM>>
}
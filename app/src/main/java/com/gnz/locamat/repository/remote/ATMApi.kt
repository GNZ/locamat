package com.gnz.locamat.repository.remote

import com.gnz.locamat.data.ATM
import io.reactivex.Single
import retrofit2.http.GET


interface ATMApi {

    @GET("data/ATM_20181005_DEV")
    fun getAtms(): Single<List<ATM>>
}
package com.gnz.locamat.repository.local

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import com.gnz.locamat.data.LocATM


interface LocalRepository {

    fun insert(ATM: LocATM)

    fun insertAll(ATMs: List<LocATM>)

    fun delete(ATM: LocATM)

    fun update(ATM: LocATM)

    fun observeAll(): LiveData<List<LocATM>>

    fun observeAllPaged(): DataSource.Factory<Int, LocATM>

    fun searchPaged(query: String): DataSource.Factory<Int, LocATM>
}
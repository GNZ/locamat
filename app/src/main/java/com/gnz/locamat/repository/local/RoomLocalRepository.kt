package com.gnz.locamat.repository.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.gnz.locamat.data.LocATM


class RoomLocalRepository(private val atmDao: ATMDao) : LocalRepository {

    override fun insert(ATM: LocATM) = atmDao.insert(ATM)

    override fun insertAll(ATMs: List<LocATM>) = atmDao.insertAll(ATMs)

    override fun delete(ATM: LocATM) = atmDao.delete(ATM)

    override fun update(ATM: LocATM) = atmDao.update(ATM)

    override fun observeAll(): LiveData<List<LocATM>> = atmDao.observeAll()

    override fun observeAllPaged(): DataSource.Factory<Int, LocATM> = atmDao.observeAllPaged()

    override fun searchPaged(query: String): DataSource.Factory<Int, LocATM> = atmDao.searchPaged("%$query%")
}
package com.gnz.locamat.repository.local

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.gnz.locamat.data.LocATM
import com.gnz.locamat.data.TABLE_NAME

@Dao
interface ATMDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ATM: LocATM)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ATMs: List<LocATM>)

    @Update
    fun update(ATM: LocATM)

    @Delete
    fun delete(ATM: LocATM)

    @Query("SELECT * FROM $TABLE_NAME")
    fun observeAll(): LiveData<List<LocATM>>

    @Query("SELECT * FROM $TABLE_NAME")
    fun observeAllPaged(): DataSource.Factory<Int, LocATM>

    @Query("SELECT * FROM $TABLE_NAME WHERE name LIKE :query " +
            "OR formatted LIKE :query")
    fun searchPaged(query: String): DataSource.Factory<Int, LocATM>
}
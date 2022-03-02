package com.oguzhancetin.goodpostureapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.oguzhancetin.goodpostureapp.data.model.Record

@Dao
interface RecordsDao {
    @Query("SELECT * FROM record")
    fun getAll(): LiveData<List<Record>>

    @Insert
    fun insert(record: Record)

    @Delete
    fun delete(record: Record)
}
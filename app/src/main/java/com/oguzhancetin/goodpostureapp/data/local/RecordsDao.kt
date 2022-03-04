package com.oguzhancetin.goodpostureapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.oguzhancetin.goodpostureapp.data.model.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDao {
    @Query("SELECT * FROM record_table")
    fun getAll(): Flow<List<Record>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(record: Record)

    @Delete
    fun delete(record: Record)
}
package com.oguzhancetin.goodpostureapp.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oguzhancetin.goodpostureapp.data.local.AppDatabase
import com.oguzhancetin.goodpostureapp.data.local.RecordsDao
import com.oguzhancetin.goodpostureapp.data.model.Record
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RecordRepository @Inject constructor(@ApplicationContext context: Context) {
    private val dao = AppDatabase.getDatabase(context).recordDao()
    val records: Flow<List<Record>> = dao.getAll()
    suspend fun insert(record: Record){
        dao.insert(record)
    }

}
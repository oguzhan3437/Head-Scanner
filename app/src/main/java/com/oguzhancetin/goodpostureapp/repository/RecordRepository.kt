package com.oguzhancetin.goodpostureapp.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.oguzhancetin.goodpostureapp.data.local.AppDatabase
import com.oguzhancetin.goodpostureapp.data.local.RecordsDao
import com.oguzhancetin.goodpostureapp.data.model.Record
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class RecordRepository @Inject constructor(@ApplicationContext context: Application) {
    private val dao = AppDatabase.getNewInstance(context).userDao()
    val records = dao.getAll()
    fun insert(record: Record){
        dao.insert(record)
    }

}
package com.oguzhancetin.goodpostureapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.oguzhancetin.goodpostureapp.data.model.Record
import com.oguzhancetin.goodpostureapp.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    val records = recordRepository.records
    fun insert(record: Record) = recordRepository.insert(record)

}
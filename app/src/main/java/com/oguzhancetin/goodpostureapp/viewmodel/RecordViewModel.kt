package com.oguzhancetin.goodpostureapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.goodpostureapp.data.model.Record
import com.oguzhancetin.goodpostureapp.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
): ViewModel() {
    val records:LiveData<List<Record>> = recordRepository.records.asLiveData()
    fun insert(record: Record) =
        viewModelScope.launch(Dispatchers.IO){
            recordRepository.insert(record)
        }


}
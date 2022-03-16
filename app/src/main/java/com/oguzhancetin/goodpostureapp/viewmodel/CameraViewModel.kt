package com.oguzhancetin.goodpostureapp.viewmodel

import android.content.Context
import androidx.camera.core.impl.CameraRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.goodpostureapp.data.model.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    val repository: com.oguzhancetin.goodpostureapp.repository.CameraRepository,
) :
    ViewModel() {
    val showCameraAlerStatus = repository.isShowCameraAlertInfoFlow.asLiveData()
    fun changeShowCameraAlertStatus(status:Boolean){
        viewModelScope.launch (Dispatchers.IO){
             repository.writePref(status)
        }
    }
    fun insert(record: Record) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(record)
        }


}
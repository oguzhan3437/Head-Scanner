package com.oguzhancetin.goodpostureapp.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.oguzhancetin.goodpostureapp.data.local.RecordsDao
import com.oguzhancetin.goodpostureapp.data.model.Record
import com.oguzhancetin.goodpostureapp.util.MyDataStore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val IS_CAMERA_ALERT_SHOW = booleanPreferencesKey("camera_alert")

class CameraRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recordDao: RecordsDao
) {

    val isShowCameraAlertInfoFlow: Flow<Boolean> = context.dataStore.data
        .map {
            it[IS_CAMERA_ALERT_SHOW] ?: true
        }

    suspend fun writePref(state: Boolean) {
        context.dataStore.edit {
            it[IS_CAMERA_ALERT_SHOW] = state

        }
    }
    suspend fun insert(record: Record) {
        recordDao.insert(record)
    }
}
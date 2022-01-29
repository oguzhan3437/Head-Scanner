package com.oguzhancetin.goodpostureapp.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

object MyDataStore{
    private const val USER_PREFERENCES_NAME = "PostureScanApp"
    val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )
}









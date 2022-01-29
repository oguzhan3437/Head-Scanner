package com.oguzhancetin.goodpostureapp.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.oguzhancetin.goodpostureapp.util.MyDataStore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val IS_USER_SEE_INTRO = booleanPreferencesKey("user_introl")

class PreferenceRepository @Inject constructor(@ApplicationContext private val context: Context) {

    val userIsSeeIntroFlow: Flow<Boolean> = context.dataStore.data
        .map {
            it[IS_USER_SEE_INTRO] ?: false
        }

    suspend fun writePref(state: Boolean) {
        context.dataStore.edit {
            it[IS_USER_SEE_INTRO] = state

        }
    }
}
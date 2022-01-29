package com.oguzhancetin.goodpostureapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.oguzhancetin.goodpostureapp.data.preference.PreferenceRepository
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: PreferenceRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val userIntoInfo: LiveData<Boolean> = repository.userIsSeeIntroFlow.asLiveData()
    fun writeUserIntroInfo(state: Boolean) {
        viewModelScope.launch {
            repository.writePref(state)
            context

        }
    }
}
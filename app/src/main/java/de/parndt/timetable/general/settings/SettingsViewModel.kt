package de.parndt.timetable.general.settings

import android.content.Context
import androidx.lifecycle.*
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
        private val context: Context,
        private val settingsUseCase: SettingsUseCase
) : ViewModel() {


    fun readPreviousLecturesSetting(): Boolean {
        return settingsUseCase.valueOfPreviousLecturesSetting()
    }

    fun updatePreviousLecturesSetting(newValue:Boolean){
        settingsUseCase.changePreviousLecturesSettings(newValue)

    }

    class Factory @Inject constructor(
            private val context: Context,
            private val settingsUseCase: SettingsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SettingsViewModel(context, settingsUseCase) as T
        }
    }
}
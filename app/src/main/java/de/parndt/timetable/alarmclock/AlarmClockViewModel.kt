package de.parndt.timetable.alarmclock

import android.content.Context
import androidx.lifecycle.*
import de.parndt.timetable.settings.SettingsUseCase
import javax.inject.Inject

class AlarmClockViewModel @Inject constructor(
    private val context: Context,
    private val settingsUseCase: SettingsUseCase
) : ViewModel() {


    fun getAlarmClockEnabled(): Boolean {
        return settingsUseCase.isAlarmEnabled()
    }

    fun setAlarmClockEnabled(value:Boolean) {
        settingsUseCase.setAlarmClockEnabled(value)
    }

    class Factory @Inject constructor(
        private val context: Context,
        private val settingsUseCase: SettingsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AlarmClockViewModel(context, settingsUseCase) as T
        }
    }
}
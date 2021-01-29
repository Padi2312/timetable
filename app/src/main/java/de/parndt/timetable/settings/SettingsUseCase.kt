package de.parndt.timetable.settings

import android.content.Context
import android.content.SharedPreferences

import javax.inject.Inject

class SettingsUseCase @Inject constructor(val context: Context) {

    private val settingPreviousLectures = "show_previous_lectures"
    private val enableAlarmClock = "enable_alarm_clock"


    private fun getSharedPrefs(): SharedPreferences? {
        return context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    private fun writeKeyValueToSharedPrefs(key: String, value: Boolean) {
        with(getSharedPrefs()?.edit()) {
            this?.putBoolean(key, value)
            this?.apply()
        }
    }

    private fun readKeyValueFromSharedPrefs(key: String): Boolean? {
        return getSharedPrefs()?.getBoolean(key, false)
    }

    fun changePreviousLecturesSettings(value: Boolean) {
        writeKeyValueToSharedPrefs(settingPreviousLectures, value)
    }

    fun valueOfPreviousLecturesSetting(): Boolean {
        return readKeyValueFromSharedPrefs(settingPreviousLectures) ?: false
    }

    fun setAlarmClockEnabled(value: Boolean) {
        writeKeyValueToSharedPrefs(enableAlarmClock, value)
    }

    fun isAlarmEnabled(): Boolean {
        return readKeyValueFromSharedPrefs(enableAlarmClock) ?: false
    }

}
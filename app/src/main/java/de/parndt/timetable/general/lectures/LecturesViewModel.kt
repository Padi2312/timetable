package de.parndt.timetable.general.lectures

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.*
import de.parndt.timetable.general.settings.SettingsUseCase
import de.parndt.timetable.general.timetable.TimeTableUseCase
import de.parndt.timetable.lecturesmodels.Lecture
import de.parndt.timetable.lecturesmodels.LecturesDay
import de.parndt.timetable.update.Updater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class LecturesViewModel @Inject constructor(
    private val context: Context,
    private val timeTableUseCase: TimeTableUseCase,
    private val settingsUseCase: SettingsUseCase,
    private val updater: Updater
) : ViewModel() {

    private val _lectures = MutableLiveData<List<LecturesDay>>()
    fun getLectures(): LiveData<List<LecturesDay>> = _lectures

    fun loadLectures() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = timeTableUseCase.getDailyLectures()
            _lectures.postValue(list)
        }
    }

    fun loadAllLectures() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = timeTableUseCase.getAllLectures()
            _lectures.postValue(list)
        }
    }

    fun getTodayLectures(): List<Lecture>? {
        val lectures = _lectures.value ?: return null
        return lectures.find { it.getDateValue() == timeTableUseCase.getCurrentDate() }
            ?.getLecturesOfDay()
    }

    fun showPreviousLecturesEnabled(): Boolean {
        return settingsUseCase.valueOfPreviousLecturesSetting()
    }

    fun getCurrentDate(): LocalDate {
        return timeTableUseCase.getCurrentDate()
    }

    fun checkForUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            updater.getUpdateInfo()
        }
    }

    fun initUpdateFunction(actions: Updater.Actions) {
        updater.initActionInterface(actions)
    }


    fun cancelDownload() {
        updater.cancelDownload()
    }

    fun startAppUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            updater.startAppUpdate()
        }
    }

    fun getInstallIntent(uri: Uri): Intent {
        return updater.getInstallIntent(uri)
    }

    class Factory @Inject constructor(
        private val context: Context,
        private val timeTableUseCase: TimeTableUseCase,
        private val settingsUseCase: SettingsUseCase,
        private val updater: Updater
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LecturesViewModel(context, timeTableUseCase, settingsUseCase, updater) as T
        }
    }

}
package de.parndt.timetable.lectures

import android.content.Context
import androidx.lifecycle.*
import de.parndt.timetable.settings.SettingsUseCase
import de.parndt.timetable.timetable.TimeTableUseCase
import de.parndt.timetable.lecturesmodels.Lecture
import de.parndt.timetable.lecturesmodels.LecturesDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class LecturesViewModel @Inject constructor(
    private val context: Context,
    private val timeTableUseCase: TimeTableUseCase,
    private val settingsUseCase: SettingsUseCase,
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


    class Factory @Inject constructor(
        private val context: Context,
        private val timeTableUseCase: TimeTableUseCase,
        private val settingsUseCase: SettingsUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LecturesViewModel(context, timeTableUseCase, settingsUseCase) as T
        }
    }

}
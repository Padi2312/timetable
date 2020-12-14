package de.parndt.timetable.general.tabs.daily

import android.content.Context
import androidx.lifecycle.*
import de.parndt.timetable.general.timetable.Lecture
import de.parndt.timetable.general.timetable.TimeTableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

class DailyFragmentViewModel @Inject constructor(
    private val context: Context,
    private val timeTableUseCase: TimeTableUseCase
) : ViewModel() {

    private val _dailyLectures = MutableLiveData<List<Lecture>>()
    private val currentDateLiveData = MutableLiveData<Pair<String, String>>()

    private var selectedDate: LocalDateTime = timeTableUseCase.getCurrentDate()

    fun getDailyLectures(): LiveData<List<Lecture>> = _dailyLectures


    fun loadDailyLectures() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = timeTableUseCase.getTodaysLectures()
            _dailyLectures.postValue(list)
            val date = timeTableUseCase.parseDateToString(selectedDate)
            val datePair =
                Pair(date, selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.GERMANY))
            currentDateLiveData.postValue(datePair)
        }
    }

    fun setCurrentDateToToday() {
        selectedDate = timeTableUseCase.getCurrentDate()
        loadLecturesByDate(selectedDate)
    }

    fun setDateWithData(year: Int, month: Int, day: Int) {
        selectedDate = selectedDate.withDayOfMonth(day)
        selectedDate = selectedDate.withMonth(month)
        selectedDate = selectedDate.withYear(year)

        loadLecturesByDate(selectedDate)
    }

    fun getNextDateLectures() {
        selectedDate = selectedDate.plusDays(1)

        val selectedDayOfWeek = selectedDate.dayOfWeek

        selectedDate = when (selectedDayOfWeek) {
            DayOfWeek.SATURDAY -> {
                selectedDate.plusDays(2)
            }
            DayOfWeek.SUNDAY -> {
                selectedDate.plusDays(1)
            }
            else -> {
                selectedDate
            }
        }
        loadLecturesByDate(selectedDate)
    }

    fun getPreviousDateLectures() {
        selectedDate = selectedDate.minusDays(1)
        if (selectedDate.dayOfWeek == DayOfWeek.SUNDAY)
            selectedDate = selectedDate.minusDays(2)
        loadLecturesByDate(selectedDate)
    }

    private fun loadLecturesByDate(dateTime: LocalDateTime) {
        val date = timeTableUseCase.parseDateToString(dateTime)
        val lectures = timeTableUseCase.getLecutresFromDate(date)
        updateLiveDatas(lectures, date)
    }

    private fun updateLiveDatas(lectures: List<Lecture>, date: String) {
        _dailyLectures.postValue(lectures)
        val datePair =
            Pair(date, selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.GERMANY))
        currentDateLiveData.postValue(datePair)
    }

    fun getCurrentDate(): String {
        return timeTableUseCase.getCurrentDateAsString()
    }

    fun currentDateChanged() = currentDateLiveData

    class Factory @Inject constructor(
        private val context: Context,
        private val timeTableUseCase: TimeTableUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DailyFragmentViewModel(context, timeTableUseCase) as T
        }
    }
}
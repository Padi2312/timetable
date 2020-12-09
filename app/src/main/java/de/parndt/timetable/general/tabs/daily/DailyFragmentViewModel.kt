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

    private val lecturesLiveData = MutableLiveData<List<Lecture>>()
    private val currentDateLiveData = MutableLiveData<Pair<String, String>>()

    private var selectedDate: LocalDateTime = timeTableUseCase.getCurrentDate()

    fun getLectures(): LiveData<List<Lecture>> = lecturesLiveData


    fun loadTodaysLectures() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = timeTableUseCase.getTodaysLectures()
            lecturesLiveData.postValue(list)
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
        jumpToNextWeek()
        loadLecturesByDate(selectedDate)
    }

    fun getPreviousDateLectures() {
        selectedDate = selectedDate.minusDays(1)
        jumpToPreviousWeek()
        loadLecturesByDate(selectedDate)
    }

    /**
     * If the current selected day is either saturday or sunday
     * then jump to the next Monday
     */
    private fun jumpToNextWeek() {
        selectedDate = if (selectedDate.dayOfWeek == DayOfWeek.SATURDAY)
            selectedDate.plusDays(2)
        else if (selectedDate.dayOfWeek == DayOfWeek.SUNDAY)
            selectedDate.plusDays(1)
        else
            selectedDate
    }

    private fun jumpToPreviousWeek() {
        selectedDate = if (selectedDate.dayOfWeek == DayOfWeek.SUNDAY)
            selectedDate.minusDays(2)
        else
            selectedDate
    }

    private fun loadLecturesByDate(dateTime: LocalDateTime) {
        val date = timeTableUseCase.parseDateToString(dateTime)
        val lectures = timeTableUseCase.getLecutresFromDate(date)
        updateLiveDatas(lectures, date)
    }

    private fun updateLiveDatas(lectures: List<Lecture>, date: String) {
        lecturesLiveData.postValue(lectures)
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
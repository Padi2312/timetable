package de.parndt.timetable.general.tabs.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.parndt.timetable.general.timetable.TimeTableUseCase
import de.parndt.timetable.general.timetable.Week
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeeklyFragmentViewModel @Inject constructor(
    private val timeTableUseCase: TimeTableUseCase
) : ViewModel() {


    private var selectedWeekNumber = timeTableUseCase.getCurrentWeekNumber()

    private val _weeklyLectures = MutableLiveData<Week>()
    fun getWeeklyLectures(): LiveData<Week> = _weeklyLectures

    fun loadCurrentWeekLectures() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = timeTableUseCase.getCurrentWeekLectures()
            selectedWeekNumber = timeTableUseCase.getCurrentWeekNumber()
            _weeklyLectures.postValue(list)
        }
    }

    fun getNextWeeklyLectures() {
        val weekNumber = selectedWeekNumber.plus(1)
        updateWeekNumber(weekNumber)
        viewModelScope.launch(Dispatchers.IO) {
            val list = timeTableUseCase.getWeeklyLecturesFromWeekNumber(selectedWeekNumber)
            _weeklyLectures.postValue(list)
        }
    }

    fun getPreviousWeeklyLectures() {
        val weekNumber = selectedWeekNumber.minus(1)
        updateWeekNumber(weekNumber)
        viewModelScope.launch(Dispatchers.IO) {
            val list = timeTableUseCase.getWeeklyLecturesFromWeekNumber(selectedWeekNumber)
            _weeklyLectures.postValue(list)
        }
    }

    private fun updateWeekNumber(weekNumber: Int) {
        selectedWeekNumber = when {
            weekNumber > timeTableUseCase.getNumberOfWeeksInYear() -> {
                1
            }
            weekNumber < 1 -> {
                timeTableUseCase.getNumberOfWeeksInYear()
            }
            else -> {
                weekNumber
            }
        }
    }

    fun isSelectedWeekCurrentWeek(): Boolean {
        return selectedWeekNumber == timeTableUseCase.getCurrentWeekNumber()
    }
}
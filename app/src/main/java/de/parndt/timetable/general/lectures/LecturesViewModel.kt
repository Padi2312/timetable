package de.parndt.timetable.general.lectures

import android.content.Context
import androidx.lifecycle.*
import de.parndt.timetable.general.timetable.TimeTableUseCase
import de.parndt.timetable.lecturesmodels.LecturesDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LecturesViewModel @Inject constructor(
        private val context: Context,
        private val timeTableUseCase: TimeTableUseCase
) : ViewModel() {

    private val _lectures = MutableLiveData<List<LecturesDay>>()
    fun getLectures(): LiveData<List<LecturesDay>> = _lectures

    fun loadLectures() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = timeTableUseCase.getDailyLectures()
            _lectures.postValue(list)
        }
    }

    fun getTodaysLecturesDay(): LecturesDay? {
        val lectures = _lectures.value ?: return null
        return lectures.find { it.getDateValue() == timeTableUseCase.getCurrentDate() }
    }


    class Factory @Inject constructor(
            private val context: Context,
            private val timeTableUseCase: TimeTableUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LecturesViewModel(context, timeTableUseCase) as T
        }
    }
}
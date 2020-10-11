package de.parndt.mydos.ui.customcomponent.datetimeselection

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class DateTimeSelectionViewModel @Inject constructor() : ViewModel() {

    private var executionDate: String? = null
    fun getDate() = executionDate

    private var executionTime: String? = null
    fun getTime() = executionTime


    fun dateEmpty(): Boolean {
        return executionDate.isNullOrEmpty()
    }

    fun timeEmpty(): Boolean {
        return executionTime.isNullOrEmpty()
    }

    fun formatDate(day: Int, month: Int, year: Int): String {
        val MM: Int = month + 1
        executionDate = if ((day < 10 && MM > 10) || (day < 10 && MM == 10))
            "0${day}.${MM}.${year}"
        else if ((MM < 10 && day > 10) || (MM < 10 && day == 10))
            "${day}.0${MM}.${year}"
        else if (day < 10 && MM < 10)
            "0${day}.0${MM}.${year}"
        else
            "${day}.${MM}.${year}"

        return executionDate!!
    }

    fun getCurrentDateTime(): DateTimeDto {
        val calendar: Calendar = Calendar.getInstance()
        return DateTimeDto(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
    }

    fun resetTime() {
        executionTime = null
    }

    fun resetDate() {
        executionDate = null
    }

    fun formatTime(hour: Int, minute: Int): String {
        executionTime = if (minute < 10 && hour > 10)
            "${hour}:0${minute}"
        else if (hour < 10 && minute > 10)
            "0${hour}:${minute}"
        else if (minute < 10 && hour < 10)
            "0${hour}:0${minute}"
        else
            "${hour}:${minute}"
        return executionTime!!
    }

    data class DateTimeDto(
        val dayOfMonth: Int,
        val month: Int,
        val year: Int,
        val hour: Int,
        val minute: Int
    )
}
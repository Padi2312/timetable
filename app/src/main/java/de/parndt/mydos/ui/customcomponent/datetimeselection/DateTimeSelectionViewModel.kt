package de.parndt.mydos.ui.customcomponent.datetimeselection

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

    fun resetExecution() {
        executionDate = null
        executionTime = null
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
}
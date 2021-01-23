package de.parndt.timetable.lecturesmodels

import de.parndt.timetable.utils.Utils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class LecturesDay(id: String, dateOfDay: String, lecturesList: List<Lecture>) {

    private var lectures: List<Lecture> = lecturesList
    private var date: String = dateOfDay
    private var uuid: String = id

    fun getUUID() = uuid

    fun getLecturesOfDay() = lectures

    open fun getDate() = date

    open fun getDateValue(): LocalDate {
        return LocalDate.parse(date, Utils.dateFormater("EE dd.MM.yy"))
    }
}

class DefaultLecturesDay(id: String, dateOfDay: String, lecturesList: List<Lecture>) : LecturesDay(id, dateOfDay, lecturesList)
class CurrentLecturesDay(id: String, dateOfDay: String, lecturesList: List<Lecture>) : LecturesDay(id, dateOfDay, lecturesList)
class PreviousLecturesDay(id: String, dateOfDay: String, lecturesList: List<Lecture>) : LecturesDay(id, dateOfDay, lecturesList)
class Weekend(id: String, dateSaturday: String, dateSunday: String, lecturesList: List<Lecture> = listOf()) : LecturesDay(id, dateSaturday, lecturesList) {

    private var saturdayDate = dateSaturday
    private var sundayDate = dateSunday

    override fun getDate(): String {
        return "$saturdayDate - $sundayDate"
    }

    override fun getDateValue(): LocalDate {
        val localeDateSaturday = LocalDate.parse(saturdayDate, Utils.dateFormater("dd.MM.yy"))
        val localeDateSunday = LocalDate.parse(sundayDate, Utils.dateFormater("dd.MM.yy"))

        return if (LocalDate.now().equals(localeDateSaturday))
            localeDateSaturday
        else
            localeDateSunday

    }
}

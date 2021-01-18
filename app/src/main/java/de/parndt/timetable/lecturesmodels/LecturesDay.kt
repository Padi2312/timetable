package de.parndt.timetable.lecturesmodels

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

sealed class LecturesDay(id: String, dateOfDay: String, lecturesList: List<Lecture>) {

    private var lectures: List<Lecture> = lecturesList
    private var date: String = dateOfDay
    private var uuid: String = id

    fun getUUID() = uuid

    fun getLecturesOfDay() = lectures

    fun getDate() = date

    fun getDateValue(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("EE dd.MM.yy")
        return LocalDate.parse(date, formatter)
    }
}

class DefaultLecturesDay(id: String, dateOfDay: String, lecturesList: List<Lecture>):LecturesDay(id,dateOfDay,lecturesList)
class CurrentLecturesDay(id: String, dateOfDay: String, lecturesList: List<Lecture>):LecturesDay(id,dateOfDay,lecturesList)
class PreviousLecturesDay(id: String, dateOfDay: String, lecturesList: List<Lecture>):LecturesDay(id,dateOfDay,lecturesList)
class WeekendDay(id: String, dateOfDay: String, lecturesList: List<Lecture> = listOf()):LecturesDay(id,dateOfDay,lecturesList)

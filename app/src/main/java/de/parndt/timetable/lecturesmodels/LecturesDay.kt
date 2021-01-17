package de.parndt.timetable.lecturesmodels

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class LecturesDay(dateOfDay: String, lecturesList: List<Lecture>) {

    private var lectures: List<Lecture> = lecturesList
    private var date: String = dateOfDay

    fun getLecturesOfDay() = lectures

    fun getDate() = date

    fun getDateValue(): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        return LocalDate.parse(date,formatter)
    }
}
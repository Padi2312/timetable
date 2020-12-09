package de.parndt.timetable.general.timetable

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeTableUseCase @Inject constructor(
    private val timetableParser: TimetableParser
) {

    private var listOfLectures: List<Lecture>? = null

    fun getCurrentDateAsString(): String {
        val current = LocalDateTime.now()
        return parseDateToString(current)
    }

    fun getCurrentDate(): LocalDateTime {
        return LocalDateTime.now()
    }

    fun parseDateToString(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        return date.format(formatter)
    }

    private fun getAllLectures(): List<Lecture> {
        if (listOfLectures == null)
            listOfLectures = timetableParser.getLectures()
        return listOfLectures!!
    }

    fun getTodaysLectures(): List<Lecture> {
        return getAllLectures().filter { it.date == getCurrentDateAsString() }
    }

    fun getLecutresFromDate(date: String): List<Lecture> {
        return getAllLectures().filter { it.date == date }
    }
}
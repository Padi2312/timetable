package de.parndt.timetable.general.timetable

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
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

    fun getCurrentWeekLectures(): Week {
        val currentWeekNumber = getCurrentWeekNumber()
        val allWeeklyLectures = timetableParser.getAllWeeklyLectures()

        return allWeeklyLectures.find { it.getCalendarWeek() == currentWeekNumber }
            ?: return Week(listOf(), 0)
    }

    fun getCurrentWeekNumber(): Int {
        val cal = getCalendarWithCurrentDate()
        return cal.get(Calendar.WEEK_OF_YEAR)
    }

    fun getNumberOfWeeksInYear(): Int {
        return getCalendarWithCurrentDate().weeksInWeekYear
    }

    private fun getCalendarWithCurrentDate(): Calendar {
        val cal: Calendar = Calendar.getInstance()
        cal.set(getCurrentDate().year, getCurrentDate().monthValue - 1, getCurrentDate().dayOfMonth)
        return cal
    }

    fun getWeeklyLecturesFromWeekNumber(weekNumber: Int): Week {
        val cal = getCalendarWithCurrentDate()
        val _weekNumber = if (weekNumber > cal.weeksInWeekYear) {
            1
        } else {
            weekNumber
        }

        val allWeeklyLectures = timetableParser.getAllWeeklyLectures()

        return allWeeklyLectures.find { it.getCalendarWeek() == _weekNumber }
            ?: return Week(listOf(), _weekNumber)

    }

    fun getLecutresFromDate(date: String): List<Lecture> {
        return getAllLectures().filter { it.date == date }
    }
}
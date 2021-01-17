package de.parndt.timetable.general.timetable

import de.parndt.timetable.lecturesmodels.Lecture
import de.parndt.timetable.lecturesmodels.LecturesDay
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

    fun getDailyLectures(): List<LecturesDay> {
        val listLecturesDay: MutableList<LecturesDay> = mutableListOf()
        val mapDateLecture = getAllLectures().groupBy { it.date }
        mapDateLecture.forEach { (t, u) ->
            listLecturesDay.add(LecturesDay(t, u))
        }
        listLecturesDay.sortBy { it.getDateValue() }
        return listLecturesDay
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

    fun getLecutresFromDate(date: String): List<Lecture> {
        return getAllLectures().filter { it.date == date }
    }
}
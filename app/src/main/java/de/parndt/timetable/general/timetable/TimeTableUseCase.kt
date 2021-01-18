package de.parndt.timetable.general.timetable

import de.parndt.timetable.lecturesmodels.Lecture
import de.parndt.timetable.lecturesmodels.LecturesDay
import java.time.LocalDate
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
        val current = LocalDate.now()
        return parseDateToString(current)
    }

    fun getCurrentDate(): LocalDate {
        return LocalDate.now()
    }


    fun getDailyLectures(): List<LecturesDay> {
        val listLecturesDay: MutableList<LecturesDay> = mutableListOf()
        val mapDateLecture = getAllLectures().groupBy { it.date }
        mapDateLecture.forEach { (t, u) ->
            val dateString = formatStringDate(t)
            listLecturesDay.add(LecturesDay(UUID.randomUUID().toString(),dateString, u))
        }
        listLecturesDay.sortBy { it.getDateValue() }
        val weekendDaysList = addWeekendDays(listLecturesDay)
        listLecturesDay.addAll(weekendDaysList)
        listLecturesDay.sortBy { it.getDateValue() }

        return listLecturesDay
    }

    private fun parseDateToString(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("EE dd.MM.yy")
        return date.format(formatter)
    }

    private fun getAllLectures(): List<Lecture> {
        if (listOfLectures == null)
            listOfLectures = timetableParser.getLectures()
        return listOfLectures!!
    }


    private fun formatStringDate(date:String): String {
        val date = parseStringToDate(date)
        val formatter = DateTimeFormatter.ofPattern("EE dd.MM.yy")
        return date.format(formatter)
    }

    private fun parseStringToDate(date:String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        return LocalDate.parse(date,formatter)
    }

    /**
     * Creates LecturesDay on weekend days depending on the inserted list.
     * List of LecturesDay have to be sorted ascending by date!
     */
    private fun addWeekendDays(listLecturesDay: List<LecturesDay>): List<LecturesDay> {
        val weekendDaysList = mutableListOf<LecturesDay>()

        var previousLecturesDay: LecturesDay? = null
        listLecturesDay.forEach {

            if (previousLecturesDay != null && it.getDateValue().dayOfMonth != previousLecturesDay!!.getDateValue().dayOfMonth + 1) {
                val saturdayDate = previousLecturesDay!!.getDateValue().plusDays(1)
                val sundayDate = previousLecturesDay!!.getDateValue().plusDays(2)

                val formatedSaturdayDate = parseDateToString(saturdayDate)
                val formatedSundayDate = parseDateToString(sundayDate)


                weekendDaysList.add(LecturesDay(UUID.randomUUID().toString(),formatedSaturdayDate, listOf()))
                weekendDaysList.add(LecturesDay(UUID.randomUUID().toString(),formatedSundayDate, listOf()))
            }

            previousLecturesDay = it
        }

        return weekendDaysList
    }

    fun getLecutresFromDate(date: String): List<Lecture> {
        return getAllLectures().filter { it.date == date }
    }
}
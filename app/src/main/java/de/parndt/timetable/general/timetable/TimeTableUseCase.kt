package de.parndt.timetable.general.timetable

import de.parndt.timetable.lecturesmodels.*
import de.parndt.timetable.utils.Logger
import de.parndt.timetable.utils.Utils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeTableUseCase @Inject constructor(
        private val timetableParser: TimetableParser
) {

    private var listOfLectures: List<Lecture>? = null
    private var listOfLecturesFromBeginning: List<Lecture>? = null

    fun getCurrentDateAsString(): String {
        val current = LocalDate.now()
        return parseDateToString(current)
    }

    fun getCurrentDate(): LocalDate {
        return LocalDate.now()
    }


    fun getAllLectures(): List<LecturesDay> {
        val listLecturesDay: MutableList<LecturesDay> = mutableListOf()
        val mapDateLecture = getAllLecturesFromBeginning().groupBy { it.date }
        mapDateLecture.forEach { (t, u) ->
            val lecturesDay = typeForLecturesDay(t, u)
            listLecturesDay.add(lecturesDay)
        }
        listLecturesDay.sortBy { it.getDateValue() }
        val weekendDaysList = addWeekendDays(listLecturesDay)
        listLecturesDay.addAll(weekendDaysList)
        listLecturesDay.sortBy { it.getDateValue() }

        return listLecturesDay
    }

    private fun typeForLecturesDay(date: String, lectures: List<Lecture>): LecturesDay {

        val dateObject = parseStringToDate(date)
        return when {
            dateObject.isBefore(getCurrentDate()) -> {
                PreviousLecturesDay(Utils.getUUIDString(), formatStringDate(date), lectures)
            }
            dateObject.isEqual(getCurrentDate()) -> {
                CurrentLecturesDay(Utils.getUUIDString(), formatStringDate(date), lectures)
            }
            dateObject.isAfter(getCurrentDate()) -> {
                DefaultLecturesDay(Utils.getUUIDString(), formatStringDate(date), lectures)
            }
            else -> {
                Logger.error("Something went terribly wrong :/")
                DefaultLecturesDay(Utils.getUUIDString(), formatStringDate(date), lectures)
            }
        }
    }

    fun getDailyLectures(): List<LecturesDay> {
        val listLecturesDay: MutableList<LecturesDay> = mutableListOf()
        val mapDateLecture = getLectures().groupBy { it.date }
        mapDateLecture.forEach { (t, u) ->
            val lecturesDay = typeForLecturesDay(t, u)
            listLecturesDay.add(lecturesDay)
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

    private fun getAllLecturesFromBeginning(): List<Lecture> {
        if (listOfLecturesFromBeginning == null)
            listOfLecturesFromBeginning = timetableParser.getAllLectures()
        return listOfLecturesFromBeginning!!
    }

    private fun getLectures(): List<Lecture> {
        if (listOfLectures == null)
            listOfLectures = timetableParser.getLectures()
        return listOfLectures!!
    }


    private fun formatStringDate(date: String): String {
        val date = parseStringToDate(date)
        val formatter = DateTimeFormatter.ofPattern("EE dd.MM.yy")
        return date.format(formatter)
    }

    private fun parseStringToDate(date: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        return LocalDate.parse(date, formatter)
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


                weekendDaysList.add(WeekendDay(Utils.getUUIDString(), formatedSaturdayDate, listOf()))
                weekendDaysList.add(WeekendDay(Utils.getUUIDString(), formatedSundayDate, listOf()))
            }

            previousLecturesDay = it
        }

        return weekendDaysList
    }

    fun getLecutresFromDate(date: String): List<Lecture> {
        return getLectures().filter { it.date == date }
    }
}
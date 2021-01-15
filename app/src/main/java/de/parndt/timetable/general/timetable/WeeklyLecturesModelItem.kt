package de.parndt.timetable.general.timetable

sealed class WeeklyLecturesModelItem

data class DayOfWeek(val day: String, val date: String) : WeeklyLecturesModelItem()
data class WeeklyLecture(val lecture: Lecture) : WeeklyLecturesModelItem()

class Week(
    private val lecturePairs: List<Pair<DayOfWeek, WeeklyLecture>>,
    private val weekNumber: Int
) {

    fun getAdapterList(): List<WeeklyLecturesModelItem> {
        val list = mutableListOf<WeeklyLecturesModelItem>()
        lecturePairs.forEach {
            if (!list.contains(it.first)) {
                list.add(it.first)
            }
            list.add(it.second)
        }
        return list
    }

    fun getParsedCalendarWeek(): String {
        return "KW $weekNumber"
    }

    fun getCalendarWeek(): Int {
        return weekNumber
    }
}
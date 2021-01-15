package de.parndt.timetable.general.timetable

import de.parndt.timetable.utils.JsoupHelper
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TimetableParser @Inject constructor() {

    private var timeTable: Document? = null


    fun getLectures(): List<Lecture> {
        val listOfLectures = mutableListOf<Lecture>()
        val rawLectures = getLectureViews(getTimetable())

        rawLectures.forEach { rawLecture ->
            val name = getNameOfLecutre(rawLecture)
            val dateTime = getDateTimeOfLecture(rawLecture)

            val time = getTimeOnly(dateTime)
            val date = getDateOnly(dateTime)

            listOfLectures.add(
                Lecture(name, date, time)
            )
        }

        return listOfLectures
    }

    fun getAllWeeklyLectures(): MutableList<Week> {
        val listOfLectures = mutableListOf<Week>()
        val rawWeekViews = getWeekViews(getTimetable())

        rawWeekViews.forEach { rawWeek ->
            val childsOfRawWeek = rawWeek.parent().children()
            val weekNumber = getWeekNumber(childsOfRawWeek)
            val dayLecturePairList = getPairDayOfWeekAndWeeklyLectures(childsOfRawWeek)

            val week =
                Week(
                    weekNumber = weekNumber,
                    lecturePairs = dayLecturePairList
                )

            listOfLectures.add(week)
        }

        return listOfLectures
    }

    fun setAndUpdateTimeTable() {
        timeTable =
            JsoupHelper.getPage("https://rapla.dhbw.de/rapla/calendar?key=25q8zGuMAw3elezlMsiegXs3Z-sCY45qHbigy7wiQ2e27FEEw1gUZrt95IawaK3jxZy_Y5bukYcuFWfh6SXaWeOzULboKmCdOurqipdCTePxrK7MHDzUEN1QHugfpBJAVAKMP3iDe18qS1sBr3NWbJHzr8hTTRxuZThgyq72Yxv-nZHBs65QFvlWbydaYESj&salt=-2070726140")
    }

    private fun getPairDayOfWeekAndWeeklyLectures(weekElements: List<Element>): MutableList<Pair<DayOfWeek, WeeklyLecture>> {
        val daysOfWeek = getDaysOfWeek(weekElements)
        val weeklyLectures = getLecturesOfWeek(daysOfWeek)

        val dayLecturePairList = mutableListOf<Pair<DayOfWeek, WeeklyLecture>>()
        daysOfWeek.forEach { dayOfWeek ->
            weeklyLectures.forEach { weeklyLecture ->
                if (weeklyLecture.lecture.date.contains(dayOfWeek.date))
                    dayLecturePairList.add(Pair(dayOfWeek, weeklyLecture))
            }
        }

        return dayLecturePairList
    }

    private fun getLecturesOfWeek(dayOfWeek: List<DayOfWeek>): List<WeeklyLecture> {
        val allLectures = getLectures()
        val weeklyLectures = mutableListOf<WeeklyLecture>()
        dayOfWeek.forEach { dayOfWeekElement ->
            val lecture = allLectures.filter {
                it.date.contains(dayOfWeekElement.date)
            }
            lecture.forEach {
                weeklyLectures.add(WeeklyLecture((it)))
            }
        }
        return weeklyLectures
    }

    private fun getWeekNumber(weekElements: List<Element>): Int {
        val weekNumberRaw = weekElements.find { it.className() == "week_number" } ?: return 0
        return weekNumberRaw.text().subSequence(3, weekNumberRaw.text().length).toString().toInt()
    }

    private fun getDaysOfWeek(weekElements: List<Element>): List<DayOfWeek> {
        val days = weekElements.filter { it.className() == "week_header" }
        val listOfDays: MutableList<DayOfWeek> = mutableListOf()

        days.forEach {
            val dayOfWeek = it.text().subSequence(0, 2).toString()
            val date = it.text().subSequence(3, it.text().length).toString()
            listOfDays.add(DayOfWeek(dayOfWeek, date))
        }
        return listOfDays
    }

    private fun getTimetable(): Document {
        if (timeTable == null) {
            setAndUpdateTimeTable()
        }
        return timeTable!!
    }

    private fun getLectureViews(document: Document): List<Element> {
        return document.select(".week_block")
    }

    private fun getWeekViews(document: Document): List<Element> {
        return document.select(".week_number")
    }

    private fun getDateTimeOfLecture(weekBlockElement: Element): String {
        val tooltip = weekBlockElement.children().select(".tooltip")
        val divWithInfo = tooltip[0].children().select("div")
        val textnode = divWithInfo[1].childNode(0)
        return (textnode as TextNode).text() ?: ""
    }

    private fun getNameOfLecutre(weekBlockElement: Element): String {
        val childs = weekBlockElement.children()
        return (childs[0].childNode(2) as TextNode).text()
    }

    private fun getTimeOnly(dateTime: String): String {
        val time = dateTime.subSequence(12, dateTime.length).toString()
        return time
    }

    private fun getDateOnly(dateTime: String): String {
        val date = dateTime.subSequence(3, 11).toString()
        return date
    }
}
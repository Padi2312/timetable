package de.parndt.timetable.general.timetable

import de.parndt.timetable.lecturesmodels.Lecture
import de.parndt.timetable.utils.JsoupHelper
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import java.util.*
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
                Lecture(UUID.randomUUID().toString(),name, date, time)
            )
        }

        return listOfLectures
    }


    fun setAndUpdateTimeTable() {
        timeTable =
            JsoupHelper.getPage("https://rapla.dhbw.de/rapla/calendar?key=25q8zGuMAw3elezlMsiegXs3Z-sCY45qHbigy7wiQ2e27FEEw1gUZrt95IawaK3jxZy_Y5bukYcuFWfh6SXaWeOzULboKmCdOurqipdCTePxrK7MHDzUEN1QHugfpBJAVAKMP3iDe18qS1sBr3NWbJHzr8hTTRxuZThgyq72Yxv-nZHBs65QFvlWbydaYESj&salt=-2070726140&today=Heute")
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
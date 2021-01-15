package de.parndt.timetable.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object JsoupHelper {

    fun getPage(url: String): Document {

        return Jsoup.connect(url).get()
    }

    fun getElements(document: Document,name:String): Elements? {
        return document.select(name)
    }

}
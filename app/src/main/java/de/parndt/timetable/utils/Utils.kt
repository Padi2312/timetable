package de.parndt.timetable.utils

import java.time.format.DateTimeFormatter
import java.util.*

object Utils {

    fun getUUIDString(): String {
        return UUID.randomUUID().toString()
    }

    fun dateFormater(pattern: String): DateTimeFormatter? {
        return DateTimeFormatter.ofPattern(pattern)
    }
}
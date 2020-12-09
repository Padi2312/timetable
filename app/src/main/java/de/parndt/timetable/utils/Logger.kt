package de.parndt.timetable.utils

import android.util.Log

object Logger {

    private val tag = "Timetable"

    fun error(message: String) {
        error(Exception(message))
    }

    fun error(ex: Throwable) {
        Log.e(tag, ex.localizedMessage, ex)
    }

    fun info(message: String) {
        Log.d(tag, message)
    }

}
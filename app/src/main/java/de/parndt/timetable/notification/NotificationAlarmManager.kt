package de.parndt.timetable.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import javax.inject.Inject


class NotificationAlarmManager @Inject constructor(private val _context: Context) {

    @SuppressLint("WrongConstant")
    fun startAlarm(c: java.util.Calendar, title: String, content: String) {
        val alarmManager = _context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(_context, NotificationReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("content", content)
        val pendingIntent = PendingIntent.getBroadcast(_context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    fun cancelAlarm() {
        val alarmManager = _context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(_context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(_context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager!!.cancel(pendingIntent)
    }
}
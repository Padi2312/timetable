package de.parndt.mydos.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.parndt.mydos.R
import java.util.*

class NotificationHelper private constructor(private val _context: Context) {


    private val CHANNEL_ID = "notify"
    private val CHANNEL_NAME = "notify_channel"
    private lateinit var notificationManager: NotificationManager

    companion object {
        private var instance: NotificationHelper? = null

        fun Instance(appContext: Context): NotificationHelper? {
            if (instance == null)
                instance = NotificationHelper(appContext)
            return instance
        }

    }


    init {
        if (!isNotificationChannelEnabled(CHANNEL_ID))
            createDefaultNotificationChannel(CHANNEL_ID, CHANNEL_NAME)
        else {
            initNotificationManager()
        }
    }


    private fun initNotificationManager() {
        notificationManager =
            _context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun createDefaultNotificationChannel(channelId: String, channelName: String) {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager =
            _context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun isNotificationChannelEnabled(channelId: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!TextUtils.isEmpty(channelId)) {
                val manager =
                    _context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = manager.getNotificationChannel(channelId)
                return channel != null
            }
            false
        } else {
            NotificationManagerCompat.from(_context).areNotificationsEnabled()
        }
    }

    fun createNotification(title: String, content: String) {
        val builder = NotificationCompat.Builder(_context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()

        NotificationManagerCompat.from(_context).apply {
            val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
            notificationManager.notify(m, builder)
        }

    }

}
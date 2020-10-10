package de.parndt.mydos.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver :
    BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent?.extras
        val title = extras?.getString("title", "") ?: ""
        val content = extras?.getString("content", "") ?: ""
        NotificationHelper.Instance(context!!)
            ?.createNotification(title = title, content = content)
    }
}
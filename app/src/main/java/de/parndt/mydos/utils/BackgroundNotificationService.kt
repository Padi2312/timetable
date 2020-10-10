package de.parndt.mydos.utils

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.widget.Toast

class BackgroundNotificationService : Service() {
    private val mBinder: LocalBinder = LocalBinder()
    private var delayTimeForNotification: Long? = null
    private var postDelayFunc: (() -> Unit)? = null
    protected var handler: Handler? = null
    protected var mToast: Toast? = null

    inner class LocalBinder : Binder() {
        val service: BackgroundNotificationService
            get() = this@BackgroundNotificationService
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handler = Handler()
        handler!!.postDelayed(
            {

            }, delayTimeForNotification!!
        )
        return START_STICKY
    }

    fun setPostDelayFunction(func: () -> Unit) = apply {
        postDelayFunc = func
    }

    fun setDelayTime(delayTime: Long) = apply {
        delayTimeForNotification = delayTime
    }
}
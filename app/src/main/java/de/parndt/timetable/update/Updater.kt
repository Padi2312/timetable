package de.parndt.timetable.update

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.google.gson.Gson
import de.parndt.timetable.utils.Utils
import okhttp3.*
import okio.BufferedSink
import okio.Okio
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import javax.inject.Inject


class Updater @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    private val context: Context
) {

    fun getUpdateInfo() {
        val appVersion = getAppVersion()
        val request =
            Request.Builder()
                .url("https://apache2.allthing.eu/timetable/update.json")
                .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val update = gson.fromJson<Update>(response.body!!.string(), Update::class.java)
            if (appVersion == update.getVersion()) {
                println("APP IST AUF NEUSTEM STAND")
            } else {
                println("APP bRAUCHT UPDATE")
            }
        }
    }

    fun downloadNewVersion(){
        val request =
            Request.Builder()
                .url("https://apache2.allthing.eu/timetable/timetable.apk")
                .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val file = File(context.cacheDir, Utils.getUUIDString())
            val sink: BufferedSink = file.sink().buffer()
            sink.writeAll(response.body!!.source())
            sink.close()
        }

    }

    fun getAppVersion(): Int {
        return try {
            val pInfo: PackageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            val version = pInfo.versionName
            version.replace(".", "").toInt()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1
        }
    }

}
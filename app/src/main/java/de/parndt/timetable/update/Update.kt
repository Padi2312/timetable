package de.parndt.timetable.update

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.gson.Gson
import de.parndt.timetable.utils.Logger
import okhttp3.*
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Update @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    private val _context: Context
) : FileProvider() {

    private lateinit var updateInfo: UpdateInfo

    private lateinit var actions: Actions

    interface Actions {
        fun updateAvailable()
        fun updateDownloaded(pathToFile: Uri)
    }

    fun initActionInterface(_actions: Actions) {
        actions = _actions
    }


    fun getUpdateInfo() {
        val request =
            Request.Builder()
                .url("https://apache2.allthing.eu/timetable/update.json")
                .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            updateInfo = gson.fromJson(response.body!!.string(), UpdateInfo::class.java)

            if (checkIfUpdateIsAvailable() && !updateInfo.experimental)
                actions.updateAvailable()
        }

    }

    private fun checkIfUpdateIsAvailable(): Boolean {
        val appVersion = getAppVersion()
        return when {
            appVersion == updateInfo.getVersionNumber() -> {
                Logger.info("App is up-to-date. No update available.")
                false
            }
            appVersion > updateInfo.getVersionNumber() -> {
                Logger.info("App is in beta-mode. Prepare for bugs...")
                false
            }
            else -> {
                Logger.info("App is outdated. Update is available")
                true
            }
        }
    }

    fun downloadNewVersion() {
        val request =
            Request.Builder()
                .url("https://apache2.allthing.eu/timetable/timetable.apk")
                .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val file = File(_context.getExternalFilesDir(null), "timetable.apk")
            val uri =
                getUriForFile(_context, "de.parndt.timetable.provider", file)
            val sink: BufferedSink = file.sink().buffer()
            sink.writeAll(response.body!!.source())
            sink.close()

            actions.updateDownloaded(uri)
        }

    }

    fun getAppVersion(): Int {
        return try {
            val pInfo: PackageInfo =
                _context.packageManager.getPackageInfo(_context.packageName, 0)
            val version = pInfo.versionName
            version.replace(".", "").toInt()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1
        }
    }

}
package de.parndt.timetable.update

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.gson.Gson
import de.parndt.timetable.utils.Logger
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.use


@Singleton
class Updater @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    private val _context: Context
) : FileProvider() {

    private lateinit var updateInfo: UpdateInfo

    //Set this true to download allow experimental version update
    private val isExperimental = true

    private lateinit var actions: Actions
    private var isCanceled = false

    interface Actions {
        fun updateAvailable(isAvailable: Boolean)
        fun downloadProgress(progress: Long, maxSize: Long)
        fun downloadComplete(pathToFile: Uri)
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

            val updateAvailable =
                (checkIfUpdateIsAvailable() && !updateInfo.experimental) || isExperimental

            actions.updateAvailable(updateAvailable)
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

    fun startAppUpdate() {
        val request =
            Request.Builder()
                .url("https://apache2.allthing.eu/timetable/timetable.apk")
                .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            downloadFile(response)

        }
    }

    fun downloadFile(response: Response) {
        val file = File(_context.getExternalFilesDir(null), "timetable.apk")
        val contentLength = response.body!!.contentLength()
        val input = response.body!!.byteStream()

        input.use { input ->
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read = 0
                var totalRead: Long = 0
                while (
                    input.read(buffer).also {
                        read = it
                        totalRead += it
                    } != -1
                ) {

                    if (isCanceled) {
                        file.delete()
                        break
                    }

                    output.write(buffer, 0, read)
                    actions.downloadProgress(totalRead, contentLength)

                }
                output.flush()
            }
        }

        if (!isCanceled)
            actions.downloadComplete(getUriForFile(_context, "de.parndt.timetable.provider", file))

    }

    fun cancelDownload() {
        isCanceled = true
    }

    fun getInstallIntent(pathToFile: Uri): Intent {
        val install = Intent(Intent.ACTION_VIEW)
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
        install.data = pathToFile
        return install
    }

    private fun getAppVersion(): Int {
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
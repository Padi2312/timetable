package de.parndt.timetable.ui

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.AndroidInjection
import de.parndt.timetable.R
import de.parndt.timetable.alarmclock.AlarmClockFragment
import de.parndt.timetable.lectures.LecturesFragment
import de.parndt.timetable.settings.SettingsFragment
import de.parndt.timetable.timetable.TimetableParser
import de.parndt.timetable.update.Updater
import de.parndt.timetable.update.ui.UpdaterDialogFragment
import de.parndt.timetable.utils.Logger
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Updater.Actions {

    @Inject
    lateinit var timetableParser: TimetableParser

    @Inject
    lateinit var updater: Updater


    private var updaterDialogFragment: UpdaterDialogFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)
        GlobalScope.launch(Dispatchers.IO) {
            timetableParser.getLectures()
        }
        initUpdateDialogFragment()
        checkForUpdates()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.settingsButton) {
            navigateToNextFragment(SettingsFragment())
        }

        if (id == R.id.alarmClock) {
            navigateToNextFragment(AlarmClockFragment())
        }
        return super.onOptionsItemSelected(item)
    }


    override fun updateAvailable(isAvailable: Boolean) {
        runOnUiThread {
            loadingIndicatorLayout.visibility = View.GONE
        }

        if (!isAvailable) {
            navigateToLectures()
        } else {
            runOnUiThread {
                showUpdateAvailable()
            }
        }
    }

    override fun downloadProgress(progress: Long, maxSize: Long) {
        updaterDialogFragment?.updateProgress(progress, maxSize)
    }

    override fun downloadComplete(pathToFile: Uri) {
        try {
            updaterDialogFragment?.dismiss()
            val install = updater.getInstallIntent(pathToFile)
            startActivity(install)
        } catch (e: Exception) {
            Logger.error(e)
        }
    }

    private fun checkForUpdates() {
        updater.initActionInterface(this)
        GlobalScope.launch(Dispatchers.IO) {
            updater.getUpdateInfo()
        }
    }


    private fun navigateToLectures() {
        //Set Main Fragment into view
        navigateToFragment(LecturesFragment())
    }

    fun navigateToFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.drawerLayout, fragment)
        fragmentTransaction.commit()
    }

    fun navigateToNextFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.drawerLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun initUpdateDialogFragment() {
        if (updaterDialogFragment == null)
            updaterDialogFragment = UpdaterDialogFragment.Instance(::updateCanceled)
    }

    private fun updateCanceled() {
        updater.cancelDownload()
    }

    private fun startAppUpdate() {
        updaterDialogFragment?.show(supportFragmentManager, "dialog_add_todo")
        GlobalScope.launch(Dispatchers.IO) {
            updater.startAppUpdate()
        }
    }

    private fun showUpdateAvailable() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Update verfügbar")
            .setMessage("Wollen sie das Update installieren?")
            .setNegativeButton("Nein") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Update") { dialog, which ->
                startAppUpdate()
            }.show()

    }


}
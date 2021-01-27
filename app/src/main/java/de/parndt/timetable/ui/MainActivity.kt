package de.parndt.timetable.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import de.parndt.timetable.R
import de.parndt.timetable.general.lectures.LecturesFragment
import de.parndt.timetable.general.settings.SettingsFragment
import de.parndt.timetable.general.timetable.TimetableParser
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var timetableParser: TimetableParser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)
        init()
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
        return super.onOptionsItemSelected(item)
    }


    fun init() {
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

}
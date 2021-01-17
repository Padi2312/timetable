package de.parndt.timetable.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import de.parndt.timetable.R
import de.parndt.timetable.general.lectures.LecturesFragment
import de.parndt.timetable.general.timetable.TimetableParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        if (id == R.id.refreshButton) {
            GlobalScope.launch(Dispatchers.IO) {
                timetableParser.setAndUpdateTimeTable()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun init() {
        navigateToFragment(LecturesFragment())

    }

    fun navigateToFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.drawerLayout, fragment)
        fragmentTransaction.commit()
    }

}
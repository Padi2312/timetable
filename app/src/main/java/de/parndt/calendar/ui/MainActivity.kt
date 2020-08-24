package de.parndt.calendar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import de.parndt.calendar.R
import de.parndt.calendar.general.start.StartFragment


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = StartFragment()
        fragmentTransaction.add(R.id.drawerLayout, fragment)
        fragmentTransaction.commit()
        AndroidInjection.inject(this)
    }


}
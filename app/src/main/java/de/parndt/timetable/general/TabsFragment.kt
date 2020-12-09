package de.parndt.timetable.general

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import de.parndt.timetable.R
import de.parndt.timetable.general.tabs.daily.DailyFragment
import de.parndt.timetable.general.tabs.weekly.WeeklyFragment
import de.parndt.timetable.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_tabs.*

class TabsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).navigateToTab(DailyFragment())
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_day -> {
                    (activity as MainActivity).navigateToTab(DailyFragment())
                    true
                }
                R.id.tab_week -> {
                    (activity as MainActivity).navigateToTab(WeeklyFragment())
                    true
                }
                else -> {
                    false
                }

            }
        }

    }

}
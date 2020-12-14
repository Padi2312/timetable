package de.parndt.timetable.general

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import dagger.android.support.AndroidSupportInjection
import de.parndt.timetable.R
import de.parndt.timetable.general.tabs.daily.DailyFragment
import de.parndt.timetable.general.tabs.weekly.WeeklyFragment
import de.parndt.timetable.ui.MainActivity
import de.parndt.timetable.utils.Logger
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

        navigation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        (activity as MainActivity).navigateToTab(DailyFragment())
                    }
                    1 -> {
                        (activity as MainActivity).navigateToTab(WeeklyFragment())
                    }
                    else -> {
                        Logger.error("No Tab Found for id ${tab.id}")
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
    }

}
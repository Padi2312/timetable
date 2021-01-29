package de.parndt.timetable.alarmclock

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import de.parndt.timetable.R
import kotlinx.android.synthetic.main.fragment_alarm_clock.*
import javax.inject.Inject

class AlarmClockFragment : Fragment() {

    @Inject
    lateinit var viewModel: AlarmClockViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm_clock, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchEnableAlarmClock?.isChecked = viewModel.getAlarmClockEnabled()

        alarmClockSettingLayout.visibility =
            if (switchEnableAlarmClock.isChecked) View.VISIBLE else View.GONE


        switchEnableAlarmClock?.setOnClickListener {
            viewModel.setAlarmClockEnabled(switchEnableAlarmClock.isChecked)
            alarmClockSettingLayout.visibility =
                if (switchEnableAlarmClock.isChecked) View.VISIBLE else View.GONE

        }
        initDropDownMenu()
    }


    private fun initDropDownMenu() {
        val items = listOf(
            AlarmClockValues.fiveMinutes.first,
            AlarmClockValues.tenMinutes.first,
            AlarmClockValues.fiveteenMinutes.first,
            AlarmClockValues.twentyMinutes.first
        )
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_alarm_clock_value, items)
        alarmValueField?.setAdapter(adapter)
    }

}
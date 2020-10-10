package de.parndt.mydos.ui.customcomponent.datetimeselection

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.utils.setDrawableEndShowLess
import de.parndt.mydos.utils.setDrawableEndShowMore
import kotlinx.android.synthetic.main.fragment_datetime_selection.*
import javax.inject.Inject

class DateTimeSelectionFragment : Fragment() {

    @Inject
    lateinit var _context: Context

    @Inject
    lateinit var viewModel: DateTimeSelectionViewModel

    private lateinit var callbacks: DateTimeSelectionCallbacks

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_datetime_selection, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newTodoExpandDatePicker.setOnClickListener {
            updateShowDatePicker()
            updateDateValue(
                newTodoDatePicker.dayOfMonth,
                newTodoDatePicker.month,
                newTodoDatePicker.year
            )
        }

        newTodoExpandTimePicker.isEnabled = false
        newTodoExpandTimePicker.setOnClickListener {
            updateShowTimePicker()
        }
        newTodoDatePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            updateDateValue(dayOfMonth, monthOfYear, year)
        }
        newTodoTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            updateTimeValue(hourOfDay, minute)
        }

        newTodoResetDate.setOnClickListener { resetDate() }
        newTodoResetTime.setOnClickListener { resetTime() }
    }

    private fun resetTime() {
        viewModel.resetTime()
        callbacks.onFormatedDateTime(viewModel.getDate(), null)
        newTodoExpandTimePicker.setText(R.string.todos_time_label)
    }

    private fun resetDate() {
        val calendar: Calendar = Calendar.getInstance()

        newTodoDatePicker.updateDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        newTodoTimePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
        newTodoTimePicker.minute = calendar.get(Calendar.MINUTE)
        newTodoExpandTimePicker.setText(R.string.todos_time_label)
        newTodoExpandTimePicker.isEnabled = false

        if (newTodoTimeLayout.visibility == View.VISIBLE)
            newTodoTimeLayout.visibility = View.GONE

        viewModel.resetDate()
        viewModel.resetTime()
        callbacks.onFormatedDate(null)
        newTodoExpandDatePicker.setText(R.string.todos_date_label)
    }

    private fun updateDateValue(day: Int, month: Int, year: Int) {
        callbacks.onDateChanged(day, month, year)

        val date = viewModel.formatDate(day, month, year)

        if (!viewModel.dateEmpty()) {
            newTodoExpandDatePicker.text = date
            callbacks.onFormatedDate(date)
            newTodoExpandTimePicker.isEnabled = true
        } else {
            newTodoExpandTimePicker.isEnabled = false
        }
    }

    private fun updateTimeValue(hour: Int, minute: Int) {
        callbacks.onTimeChanged(hour, minute)

        val time = viewModel.formatTime(hour, minute)

        if (!viewModel.timeEmpty()) {
            newTodoExpandTimePicker.text = "$time Uhr"
            callbacks.onFormatedDateTime(viewModel.getDate(), time)
        }
    }

    private fun updateShowDatePicker() {
        if (newTodoDateLayout.visibility == View.GONE) {
            newTodoDateLayout.visibility = View.VISIBLE
            setDrawableEndShowLess(newTodoExpandDatePicker, _context)
        } else {
            newTodoDateLayout.visibility = View.GONE
            setDrawableEndShowMore(newTodoExpandDatePicker, _context)
            if (viewModel.dateEmpty())
                newTodoTimeLayout.visibility = View.GONE
        }
    }

    private fun updateShowTimePicker() {
        if (newTodoTimeLayout.visibility == View.GONE) {
            newTodoTimeLayout.visibility = View.VISIBLE
            setDrawableEndShowLess(newTodoExpandTimePicker, _context)
        } else {
            newTodoTimeLayout.visibility = View.GONE
            setDrawableEndShowMore(newTodoExpandTimePicker, _context)
        }
    }


    fun setCallbackInterface(_callbacks: DateTimeSelectionCallbacks) = apply {
        this.callbacks = _callbacks
    }


    interface DateTimeSelectionCallbacks {
        fun onDateChanged(dayOfMonth: Int, month: Int, year: Int) {}
        fun onTimeChanged(hours: Int, minutes: Int, date: String? = null) {}
        fun onFormatedDate(date: String?)
        fun onFormatedDateTime(date: String?, time: String?)
    }
}
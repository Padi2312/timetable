package de.parndt.timetable.general.tabs.daily

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import de.parndt.timetable.R
import kotlinx.android.synthetic.main.tab_fragment_daily.*
import javax.inject.Inject

class DailyFragment : Fragment() {

    @Inject
    lateinit var viewModel: DailyFragmentViewModel

    private lateinit var adapter: DailyLecutresListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab_fragment_daily, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCurrentDate()
        initPreviousNextButtons()

        adapter = DailyLecutresListAdapter(requireContext())
        dailyFragmentLecutres.adapter = adapter
        dailyFragmentLecutres.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        //Observe the lectures of the selected date
        viewModel.getDailyLectures().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            loadingIndicator.visibility = View.GONE
        }

        viewModel.currentDateChanged().observe(viewLifecycleOwner) {
            setSelectedDate(it)

            if (isDateOfToday(it)) {
                hideCurrentDateButton()
            } else {
                showCurrentDateButton()
            }
        }

        dailyFragmentButtonToday.setOnClickListener {
            clickCurrentDateButton()
        }

        //Inital load of todays lectures
        viewModel.loadDailyLectures()
    }

    private fun setSelectedDate(date: Pair<String, String>) {
        dailyFragmentCurrentDate.text = "${date.second} ${date.first}"
    }

    private fun isDateOfToday(date: Pair<String, String>): Boolean {
        return viewModel.getCurrentDate() == date.first
    }

    private fun initCurrentDate() {
        setSelectedDate(Pair(viewModel.getCurrentDate(), ""))
        dailyFragmentCurrentDate.setOnClickListener {
            openDatePickerDialog()
        }
    }

    private fun showCurrentDateButton() {
        dailyFragmentButtonToday.visibility = View.VISIBLE
    }

    private fun hideCurrentDateButton() {
        dailyFragmentButtonToday.visibility = View.GONE
    }

    private fun clickCurrentDateButton() {
        loadingIndicator.visibility = View.VISIBLE
        viewModel.setCurrentDateToToday()
    }

    private fun initPreviousNextButtons(){
        dailyFragmentButtonPreviousWeek.setOnClickListener {
            loadingIndicator.visibility = View.VISIBLE
            viewModel.getPreviousDateLectures()

        }

        dailyFragmentButtonNextWeek.setOnClickListener {
            loadingIndicator.visibility = View.VISIBLE
            viewModel.getNextDateLectures()

        }
    }

    private fun openDatePickerDialog() {
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR)
        val mMonth: Int = c.get(Calendar.MONTH)
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireActivity(),
            android.R.style.Theme_Dialog,
            { _, year, monthOfYear, dayOfMonth ->
                viewModel.setDateWithData(year, monthOfYear+1, dayOfMonth)
            },
            mYear, mMonth, mDay
        )
        dpd.show()
    }

}
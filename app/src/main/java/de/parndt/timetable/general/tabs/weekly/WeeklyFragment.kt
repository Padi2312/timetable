package de.parndt.timetable.general.tabs.weekly

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import de.parndt.timetable.R
import de.parndt.timetable.general.timetable.Week
import kotlinx.android.synthetic.main.tab_fragment_weekly.*
import javax.inject.Inject

class WeeklyFragment : Fragment() {

    @Inject
    lateinit var viewModel: WeeklyFragmentViewModel

    private lateinit var adapter: WeeklyLecutresListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_fragment_weekly, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtonClickListener()
        weeklyLoadingIndicator.visibility = View.VISIBLE
        adapter = WeeklyLecutresListAdapter(requireContext())

        weeklyFragmentLecutres.adapter = adapter

        weeklyFragmentLecutres.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        viewModel.getWeeklyLectures().observe(viewLifecycleOwner) {
            setWeeklyLecturesInView(it)

            if (viewModel.isSelectedWeekCurrentWeek())
                hideCurrentDateButton()
            else
                showCurrentDateButton()
        }

        viewModel.loadCurrentWeekLectures()
    }

    private fun initButtonClickListener() {
        weeklyFragmentButtonNextWeek.setOnClickListener {
            weeklyLoadingIndicator.visibility = View.VISIBLE
            viewModel.getNextWeeklyLectures()
        }

        weeklyFragmentButtonPreviousWeek.setOnClickListener {
            weeklyLoadingIndicator.visibility = View.VISIBLE
            viewModel.getPreviousWeeklyLectures()
        }

        weeklyFragmentButtonToday.setOnClickListener {
            weeklyLoadingIndicator.visibility = View.VISIBLE
            viewModel.loadCurrentWeekLectures()
        }
    }

    private fun setWeeklyLecturesInView(week: Week) {

        if (week.getCalendarWeek() == 0) {
            weeklyFragmentWeekOfYear.text = "Keine Daten vorhanden."
            adapter.submitList(listOf())
        } else {
            weeklyFragmentWeekOfYear.text = week.getParsedCalendarWeek()
            adapter.submitList(week.getAdapterList())
        }
        weeklyLoadingIndicator.visibility = View.GONE
    }

    private fun showCurrentDateButton() {
        weeklyFragmentButtonToday.visibility = View.VISIBLE
    }

    private fun hideCurrentDateButton() {
        weeklyFragmentButtonToday.visibility = View.GONE
    }
}
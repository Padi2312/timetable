package de.parndt.timetable.general.lectures

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import de.parndt.timetable.R
import kotlinx.android.synthetic.main.fragment_lectures.*
import javax.inject.Inject

class LecturesFragment : Fragment() {


    @Inject
    lateinit var viewModel: LecturesViewModel

    private lateinit var adapter: LecutresListAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lectures, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LecutresListAdapter(requireContext())
        lecturesList.adapter = adapter
        lecturesList.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)



        viewModel.getLectures().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            setTodaysLectures()
            scrollToCurrentDay()
            lecturesLoadingIndicator.visibility = View.GONE
        }
        loadLecturesDependingOnOptions()


        lecturesExpandingBar.setOnClickListener {

            lecturesExpandingLayout.visibility = if (lecturesExpandingLayout.visibility == View.GONE) {
                lecturesShowMoreIcon.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(),
                                R.drawable.ic_baseline_expand_less_24)
                )
                View.VISIBLE
            } else {
                lecturesShowMoreIcon.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(),
                                R.drawable.ic_baseline_expand_more_24)
                )
                View.GONE
            }
        }
    }


    private fun scrollToCurrentDay() {
        val positon = adapter.getPositionOfItemByDate(viewModel.getCurrentDate())
        lecturesList.scrollToPosition(positon)
    }

    private fun loadLecturesDependingOnOptions() {
        if (viewModel.showPreviousLecturesEnabled()) {
            viewModel.loadAllLectures()
        } else {
            viewModel.loadLectures()
        }
    }

    private fun setTodaysLectures() {

        fun getSeperatorView(): View {
            val seperator = View(requireContext())
            seperator.setBackgroundColor(Color.GRAY)
            seperator.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2)
            return seperator
        }

        val todayLectures = viewModel.getTodayLectures()

        if (todayLectures == null || todayLectures.isEmpty()) {
            lecturesNoLecturesLabel.visibility = View.VISIBLE
            lecturesLecturesOfDay.visibility = View.GONE
        } else {
            for (i in todayLectures.indices) {
                val lecturesView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.list_item_lecture, null, true)

                lecturesView.findViewById<TextView>(R.id.lectureName).text = todayLectures[i].name
                lecturesView.findViewById<TextView>(R.id.lectureTime).text = todayLectures[i].time

                lecturesLecturesOfDay.addView(lecturesView)

                if (i != todayLectures.size - 1) {
                    lecturesLecturesOfDay.addView(getSeperatorView())
                }
            }
        }

    }

}
package de.parndt.timetable.general.lectures

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
            lecturesLoadingIndicator.visibility = View.GONE
        }

        viewModel.loadLectures()

    }

    private fun setTodaysLectures() {

        fun getSeperatorView(): View {
            val seperator = View(requireContext())
            seperator.setBackgroundColor(Color.GRAY)
            seperator.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2)
            return seperator
        }

        val currentLecturesDay = viewModel.getTodaysLecturesDay()

        if (currentLecturesDay == null || currentLecturesDay.getLecturesOfDay().isEmpty())
            lecturesNoLecturesLabel.visibility = View.VISIBLE
        else {
            val todaysLectures = currentLecturesDay.getLecturesOfDay()

            for (i in todaysLectures.indices) {

                val lecturesView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.list_item_lecture, null, true)

                lecturesView.findViewById<TextView>(R.id.lectureName).text = todaysLectures[i].name
                lecturesView.findViewById<TextView>(R.id.lectureTime).text = todaysLectures[i].time

                lecturesLecturesOfDay.addView(lecturesView)

                if (i != todaysLectures.size - 1) {
                    lecturesLecturesOfDay.addView(getSeperatorView())
                }
            }
        }

    }

}
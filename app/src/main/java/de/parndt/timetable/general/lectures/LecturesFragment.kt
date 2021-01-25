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
import de.parndt.timetable.update.Updater
import kotlinx.android.synthetic.main.fragment_lectures.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LecturesFragment : Fragment() {


    @Inject
    lateinit var viewModel: LecturesViewModel

    @Inject
    lateinit var updater: Updater


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
            scrollToCurrentDay()
            lecturesLoadingIndicator.visibility = View.GONE
        }
        loadLecturesDependingOnOptions()

        GlobalScope.launch { updater.getUpdateInfo() }

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


}
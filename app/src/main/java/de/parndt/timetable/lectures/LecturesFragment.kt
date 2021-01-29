package de.parndt.timetable.lectures

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
            scrollToCurrentDay()
            lecturesLoadingIndicator.visibility = View.GONE
        }
        loadLecturesDependingOnOptions()

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
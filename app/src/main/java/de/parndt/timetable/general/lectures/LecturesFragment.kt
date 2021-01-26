package de.parndt.timetable.general.lectures

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.AndroidSupportInjection
import de.parndt.timetable.R
import de.parndt.timetable.update.Update
import de.parndt.timetable.utils.Logger
import kotlinx.android.synthetic.main.fragment_lectures.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class LecturesFragment : Fragment(), Update.Actions {

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

        viewModel.initUpdateFunction(this)
        viewModel.checkForUpdates()

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

    private fun showUpdateAvailable() {
        requireActivity().runOnUiThread {
            MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Update verfügbar")
                    .setMessage("Wollen sie das Update installieren?")
                    .setNegativeButton("Nein") { dialog, which ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Update") { dialog, which ->
                        viewModel.updateApp()
                    }
                    .show()
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

    override fun updateAvailable() {

        showUpdateAvailable()
    }

    override fun updateDownloaded(pathToFile: Uri) {
        try {
            val install = Intent(Intent.ACTION_VIEW)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            install.data = pathToFile
            requireActivity().startActivity(install)
        } catch (e: Exception) {
            Logger.error(e)
        }

    }


}
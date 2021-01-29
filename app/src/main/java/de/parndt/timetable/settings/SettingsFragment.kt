package de.parndt.timetable.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import de.parndt.timetable.R
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsShowPreviousLectures.isChecked = viewModel.readPreviousLecturesSetting()
        settingsShowPreviousLectures.setOnClickListener {
            viewModel.updatePreviousLecturesSetting(settingsShowPreviousLectures.isChecked)
        }
    }


}
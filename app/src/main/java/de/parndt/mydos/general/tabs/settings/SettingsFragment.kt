package de.parndt.mydos.general.tabs.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.notification.NotificationAlarmManager
import de.parndt.mydos.repository.SettingsRepository
import kotlinx.android.synthetic.main.tab_fragment_settings.*
import javax.inject.Inject


class SettingsFragment : Fragment() {

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    @Inject
    lateinit var notificationAlarmManager: NotificationAlarmManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab_fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsChanged()

        settingsViewModel.getSettingForKey(SettingsRepository.Settings.SHOW_DELETED_TODOS){
            settingsOptionShowDeletedTodos?.isChecked = it.value

        }
        settingsViewModel.getSettingForKey(SettingsRepository.Settings.DELETE_ONCHECK){
            settingsOptionDeleteOnCheckTodo?.isChecked = it.value

        }
        settingsViewModel.getSettingForKey(SettingsRepository.Settings.SHOW_DONE_TODOS){
            settingsOptionShowDoneTodos?.isChecked = it.value

        }



    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun settingsChanged() {
        settingsOptionShowDeletedTodos.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.updateSettingWithKey(
                SettingsRepository.Settings.SHOW_DELETED_TODOS,
                isChecked
            )
        }

        settingsOptionShowDoneTodos.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.updateSettingWithKey(
                SettingsRepository.Settings.SHOW_DONE_TODOS,
                isChecked
            )
        }

        settingsOptionDeleteOnCheckTodo.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.updateSettingWithKey(
                SettingsRepository.Settings.DELETE_ONCHECK,
                isChecked
            )
        }
    }

}
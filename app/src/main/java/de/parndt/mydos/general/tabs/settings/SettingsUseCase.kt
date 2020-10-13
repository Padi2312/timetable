package de.parndt.mydos.general.tabs.settings

import de.parndt.mydos.database.models.settings.SettingsEntity
import de.parndt.mydos.repository.SettingsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    fun settingsInitialized(): Boolean {
        return settingsRepository.settingsInitialized()
    }

    fun getSettingForKey(settingKey: SettingsRepository.Settings): SettingsEntity {
        return settingsRepository.getSetting(settingKey)
    }

    suspend fun createSettingWithKey(settingKey: SettingsRepository.Settings, value: Boolean) {
        settingsRepository.createSetting(settingKey, value)
    }

    suspend fun updateSettingWithKey(settingKey: SettingsRepository.Settings, value: Boolean) {
        settingsRepository.updateSetting(settingKey, value)
    }

    fun createInitialSettings() {
        GlobalScope.launch {
            createSettingWithKey(
                SettingsRepository.Settings.SHOW_DELETED_TODOS,
                false
            )
            createSettingWithKey(
                SettingsRepository.Settings.DELETE_ONCHECK,
                false
            )
            createSettingWithKey(
                SettingsRepository.Settings.SHOW_DONE_TODOS,
                false
            )
        }

    }
}
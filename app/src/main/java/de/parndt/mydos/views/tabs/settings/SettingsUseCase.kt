package de.parndt.mydos.views.tabs.settings

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
                SettingsRepository.Settings.FILTER_ONLY_UNCHECKED,
                false
            )

            createSettingWithKey(
                SettingsRepository.Settings.FILTER_BY_PRIORITY,
                false
            )

            createSettingWithKey(
                SettingsRepository.Settings.FILTER_BY_DATE,
                true
            )
        }

    }
}
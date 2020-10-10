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

    fun getSettingForKey(settingKey: SettingsRepository.Filter): SettingsEntity {
        return settingsRepository.getSetting(settingKey)
    }

    suspend fun createSettingWithKey(settingKey: SettingsRepository.Filter, value: Boolean) {
        settingsRepository.createSetting(settingKey, value)
    }

    suspend fun updateSettingWithKey(settingKey: SettingsRepository.Filter, value: Boolean) {
        settingsRepository.updateSetting(settingKey, value)
    }

    fun createInitialSettings() {
        GlobalScope.launch {
            createSettingWithKey(
                SettingsRepository.Filter.FILTER_ONLY_UNCHECKED,
                false
            )

            createSettingWithKey(
                SettingsRepository.Filter.FILTER_BY_PRIORITY,
                false
            )

            createSettingWithKey(
                SettingsRepository.Filter.FILTER_BY_DATE,
                true
            )
        }

    }
}
package de.parndt.mydos.views.tabs.settings

import de.parndt.mydos.database.models.settings.SettingsEntity
import de.parndt.mydos.repository.SettingsRepository
import javax.inject.Inject

class SettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    fun settingsInitialized(): Boolean {
        return settingsRepository.settingsInitialized()
    }

    suspend fun getSettingForKey(settingKey: SettingsRepository.Settings): SettingsEntity {
        return settingsRepository.getSetting(settingKey)
    }

    suspend fun createSettingWithKey(settingKey: SettingsRepository.Settings, value: Boolean) {
        settingsRepository.createSetting(settingKey, value)
    }

    suspend fun updateSettingWithKey(settingKey: SettingsRepository.Settings, value: Boolean) {
        settingsRepository.updateSetting(settingKey, value)
    }

}
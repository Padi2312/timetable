package de.parndt.mydos.repository

import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.database.models.settings.SettingsEntity
import de.parndt.mydos.database.persistence.SettingsDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    database: MydosDatabase
) {

    private var dao: SettingsDao = database.settingsDao()

    fun getSettings(): List<SettingsEntity> {
        return dao.getAll()
    }

    fun getSetting(
        setting: Settings
    ): SettingsEntity {
        return dao.getSetting(setting.name)
    }

    fun settingsInitialized(): Boolean {
        val empty = dao.isEmpty()?.value
        return empty != null
    }

    suspend fun createSetting(key: Settings, value: Boolean): Long {
        val settingForEntity = key.name
        val settingsEntity = SettingsEntity(setting = settingForEntity, value = value)
        return dao.insertSetting(settingsEntity)
    }

    suspend fun updateSetting(key: Settings, value: Boolean, settingUpdated: () -> Unit = {}) {
        GlobalScope.launch {
            val setting = getSetting(key)
            setting.value = value
            dao.updateSetting(setting)
            settingUpdated()
        }
    }

    enum class Settings {
        SHOW_DELETED_TODOS,
        SHOW_DONE_TODOS,
        DELETE_ONCHECK
    }
}
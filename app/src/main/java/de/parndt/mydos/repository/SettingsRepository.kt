package de.parndt.mydos.repository

import android.content.Context
import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.database.persistence.SettingsDao
import de.parndt.mydos.database.models.settings.SettingsEntity
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
        setting: Filter
    ): SettingsEntity {
        return dao.getSetting(setting.name)
    }

    fun settingsInitialized(): Boolean {
        val empty = dao.isEmpty()?.value
        return empty != null
    }

    suspend fun createSetting(key: Filter, value: Boolean): Long {
        val settingForEntity = key.name
        val settingsEntity = SettingsEntity(setting = settingForEntity, value = value)
        return dao.insertSetting(settingsEntity)
    }

    suspend fun updateSetting(key: Filter, value: Boolean, settingUpdated: () -> Unit = {}) {
        GlobalScope.launch {
            val setting = getSetting(key)
            setting.value = value
            dao.updateSetting(setting)
            settingUpdated()
        }
    }

    enum class Filter {
        FILTER_ONLY_UNCHECKED,
        FILTER_BY_PRIORITY,
        FILTER_BY_DATE
    }

    fun Filter.getString(appcontext: Context): String {
        return when (this) {
            Filter.FILTER_BY_DATE -> appcontext.getString(de.parndt.mydos.R.string.todos_filter_by_date)
            Filter.FILTER_BY_PRIORITY -> appcontext.getString(de.parndt.mydos.R.string.todos_filter_by_priority)
            else -> ""
        }
    }

}
package de.parndt.mydos.repository

import android.content.Context
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
        FILTER_BY_EXECUTION_DATE,
        FILTER_BY_DATE_CREATED
    }

    fun Filter.getString(appcontext: Context): String {
        return when (this) {
            Filter.FILTER_BY_DATE_CREATED -> appcontext.getString(de.parndt.mydos.R.string.todos_sort_by_date_created)
            Filter.FILTER_BY_PRIORITY -> appcontext.getString(de.parndt.mydos.R.string.todos_sort_by_priority)
            else -> ""
        }
    }

}
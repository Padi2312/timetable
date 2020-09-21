package de.parndt.mydos.repository

import androidx.lifecycle.LiveData
import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.database.models.settings.SettingsDao
import de.parndt.mydos.database.models.settings.SettingsEntity
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    database: MydosDatabase
) {

    private var dao: SettingsDao = database.settingsDao()

    fun getSettings(): List<SettingsEntity> {
        return dao.getAll()
    }

    fun getSettingWithKey(setting: String) {

    }

    fun areSettingsSet():Boolean{
        val empty = dao.isEmpty()?.value
        return empty == null
    }

    suspend fun setSetting(key:String, value:String): Long {
        val settingsEntity = SettingsEntity(setting = key,value = value)
        return dao.insertSetting(settingsEntity)
    }
}
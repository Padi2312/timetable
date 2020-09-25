package de.parndt.mydos.database.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.parndt.mydos.database.models.settings.SettingsEntity


@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings")
    fun getAll(): List<SettingsEntity>

    @Query("SELECT * FROM settings ORDER BY id LIMIT 1")
    fun isEmpty(): LiveData<SettingsEntity?>?

    @Insert
    suspend fun insertSetting(settingsEntity: SettingsEntity): Long

    @Query("SELECT * FROM settings WHERE setting = :setting")
    fun getSetting(setting: String): SettingsEntity

    @Update
    suspend fun updateSetting(settingsEntity: SettingsEntity)

}
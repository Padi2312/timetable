package de.parndt.mydos.database.models.settings

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings")
    fun getAll(): List<SettingsEntity>

    @Query("SELECT * FROM settings ORDER BY id LIMIT 1")
    fun isEmpty(): LiveData<SettingsEntity?>?

    @Insert
    suspend fun insertSetting(todo: SettingsEntity): Long

}
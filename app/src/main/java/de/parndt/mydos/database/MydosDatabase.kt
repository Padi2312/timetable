package de.parndt.mydos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.parndt.mydos.database.persistence.SettingsDao
import de.parndt.mydos.database.models.settings.SettingsEntity
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.persistence.TodoDao


@Database(entities = [TodoEntity::class, SettingsEntity::class], version = 3, exportSchema = false)
abstract class MydosDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun settingsDao(): SettingsDao

}
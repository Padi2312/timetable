package de.parndt.mydos.database.models.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
class SettingsEntity(setting: String, value: Boolean = false) {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "setting")
    var setting: String = setting

    @ColumnInfo(name = "value")
    var value: Boolean = value

}
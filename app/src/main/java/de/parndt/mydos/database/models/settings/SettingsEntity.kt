package de.parndt.mydos.database.models.settings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    @ColumnInfo(name = "setting")
    var setting:String,
    @ColumnInfo(name = "value")
    var value: String
)
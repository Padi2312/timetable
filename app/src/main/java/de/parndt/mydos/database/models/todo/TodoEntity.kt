package de.parndt.mydos.database.models.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "todos")
data class TodoEntity(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    @ColumnInfo(name = "title")
    var title:String,
    @ColumnInfo(name = "priority")
    var priority: String = TodoPriority.DEFAULT.name,
    @ColumnInfo(name = "content")
    var content: String? = null,
    @ColumnInfo(name = "done")
    var done:Boolean = false
)



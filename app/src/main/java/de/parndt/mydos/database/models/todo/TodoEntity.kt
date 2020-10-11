package de.parndt.mydos.database.models.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Entity(tableName = "todos")
data class TodoEntity(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "priority")
    var priority: String = TodoPriority.DEFAULT.name,
    @ColumnInfo(name = "content")
    var content: String? = null,
    @ColumnInfo(name = "done")
    var done: Boolean = false,
    @ColumnInfo
    var executionDate: String? = null,
    @ColumnInfo
    var executionTime: String? = null,
    @ColumnInfo
    var dateCreated: String = DateTimeFormatter
        .ofPattern("dd.MM.yyyy HH:mm:ss")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now()),
    @ColumnInfo
    var deleted: Boolean = false

)


fun TodoEntity.getDateObject(){

}

package de.parndt.mydos.database.persistence

import androidx.room.*
import de.parndt.mydos.database.models.todo.TodoEntity

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getAll(): List<TodoEntity>

    @Insert
    suspend fun insertTodo(todo: TodoEntity): Long

    @Update
    suspend fun updateStatus(todo: TodoEntity)

    @Delete
    fun deleteTodo(todo: TodoEntity)

    @Query("SELECT * FROM todos WHERE todos.id == :todoId")
    fun getById(todoId: Int): TodoEntity
}
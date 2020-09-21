package de.parndt.mydos.database.models.todo

import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getAll(): List<TodoEntity>

    @Insert
    suspend fun insertTodo(todo: TodoEntity): Long

    @Update
    suspend fun updateStatus(todo: TodoEntity)

    @Delete
    fun deleteTodo(vararg todo: TodoEntity)

    @Query("SELECT * FROM todos WHERE todos.id == :todoId")
    fun getById(todoId: Int): TodoEntity
}
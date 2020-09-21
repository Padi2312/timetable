package de.parndt.mydos.repository

import android.content.Context
import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoDao
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TodoRepository @Inject constructor(
    database: MydosDatabase
) {

    private var todoDao: TodoDao = database.todoDao()

    suspend fun createNewTodo(newTodo: TodoEntity): Long {
        return todoDao.insertTodo(newTodo)
    }

    fun getAllTodos(): List<TodoEntity> {
        return todoDao.getAll()
    }

    suspend fun updateTodo(todoId:Int,newStatus:Boolean) {
        val todo = getTodoById(todoId)
        todo.done = newStatus
        return todoDao.updateStatus(todo)
    }

    fun getTodoById(todoId: Int):TodoEntity{
        return todoDao.getById(todoId)
    }
}
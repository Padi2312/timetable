package de.parndt.mydos.repository

import androidx.lifecycle.MutableLiveData
import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.persistence.TodoDao
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TodoRepository @Inject constructor(
    database: MydosDatabase
) {

    private var todoDao: TodoDao = database.todoDao()
    private var _newTodoObserver = MutableLiveData<TodoEntity>()


    fun observeNewTodos() = _newTodoObserver

    suspend fun createNewTodo(newTodo: TodoEntity): Long {
        _newTodoObserver.postValue(newTodo)
        return todoDao.insertTodo(newTodo)
    }

    fun getAllTodos(): MutableList<TodoEntity> {
        val list = todoDao.getWithoutDeletedFlag()
        list.sortBy { it.dateCreated }
        list.reverse()
        return list
    }

    suspend fun updateTodoStatus(todoId: Int, newStatus: Boolean) {
        val todo = getTodoById(todoId)
        todo.done = newStatus
        todoDao.update(todo)
    }

    suspend fun updateDeletedFlag(todoId: Int, newStatus: Boolean) {
        val todo = getTodoById(todoId)
        todo.apply {
            deleted = newStatus
        }
        todoDao.update(todo)
    }

    fun getTodoById(todoId: Int): TodoEntity {
        return todoDao.getById(todoId)
    }

    suspend fun removeTodoEntry(todo: TodoEntity) {
        todo.apply {
            deleted = true
        }
        todoDao.update(todo)
    }

}


package de.parndt.mydos.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.persistence.SettingsDao
import de.parndt.mydos.database.persistence.TodoDao
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TodoRepository @Inject constructor(
    database: MydosDatabase
) {

    private var todoDao: TodoDao = database.todoDao()
    private var settingsDao: SettingsDao = database.settingsDao()

    suspend fun createNewTodo(newTodo: TodoEntity): Long {
        return todoDao.insertTodo(newTodo)
    }

    suspend fun getAllTodos(
    ): List<TodoEntity> {
        val listOfTodos = todoDao.getAll()

        val setting = settingsDao.getSetting(SettingsRepository.Settings.FILTER_ONLY_CHECKED.name)
        if (setting.value)
            return listOfTodos.filter { !it.done }
        else
            return listOfTodos
    }

    suspend fun updateTodo(todoId: Int, newStatus: Boolean) {
        val todo = getTodoById(todoId)
        todo.done = newStatus
        return todoDao.updateStatus(todo)
    }

    fun getTodoById(todoId: Int): TodoEntity {
        return todoDao.getById(todoId)
    }

    fun removeTodoEntry(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }

}


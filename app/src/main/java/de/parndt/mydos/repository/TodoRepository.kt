package de.parndt.mydos.repository

import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
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

        val settingFilterOnlyUnchecked =
            settingsDao.getSetting(SettingsRepository.Settings.FILTER_ONLY_UNCHECKED.name).value
        val settingFilterOnlyPriority =
            settingsDao.getSetting(SettingsRepository.Settings.FILTER_BY_PRIORITY.name).value

        if (settingFilterOnlyUnchecked && !settingFilterOnlyPriority)
            return listOfTodos.filter { !it.done }
        else if (settingFilterOnlyPriority && !settingFilterOnlyUnchecked)
            return listOfTodos.sortedBy { TodoPriority.valueOf(it.priority) }
        else if (settingFilterOnlyUnchecked && settingFilterOnlyPriority) {
            val filteredList = listOfTodos.filter { !it.done }
            return filteredList.sortedBy { TodoPriority.valueOf(it.priority) }
        } else
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


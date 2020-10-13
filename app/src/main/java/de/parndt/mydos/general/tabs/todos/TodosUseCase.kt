package de.parndt.mydos.general.tabs.todos

import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.repository.SettingsRepository
import de.parndt.mydos.repository.TodoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodosUseCase @Inject constructor(
    private var todoRepository: TodoRepository,
    private var settingsRepository: SettingsRepository
) {

    fun observeNewTodos() = todoRepository.observeNewTodos()

    fun getAllTodos(): MutableList<TodoEntity> {
        var listOfTodos = todoRepository.getAllTodos().sortedBy { it.dateCreated }.reversed()
        val settingsShowDoneTodos =
            settingsRepository.getSetting(SettingsRepository.Settings.SHOW_DONE_TODOS).value

        val settingsShowDeletedTodos =
            settingsRepository.getSetting(SettingsRepository.Settings.SHOW_DELETED_TODOS).value

        if (settingsShowDeletedTodos)
            listOfTodos = todoRepository.getAllTodosWithDeleted()

        if (!settingsShowDoneTodos)
            listOfTodos = listOfTodos.filter { !it.done }

        return listOfTodos.toMutableList()
    }

    fun getTodoById(todoId: Int): TodoEntity {
        return todoRepository.getTodoById(todoId)
    }

    suspend fun updateSettingWithKey(
        settingKey: SettingsRepository.Settings,
        value: Boolean,
        settingsUpdated: () -> Unit = {}
    ) {
        settingsRepository.updateSetting(settingKey, value, settingsUpdated)
    }

    suspend fun updateTodoEntry(todoId: Int, newStatus: Boolean) =
        todoRepository.updateTodoStatus(todoId, newStatus)

    suspend fun deleteTodoEntry(todo: TodoEntity) = todoRepository.removeTodoEntry(todo)

    suspend fun undoDeleteTodo(todo: TodoEntity) {
        todoRepository.updateDeletedFlag(todo.id, false)
    }

}
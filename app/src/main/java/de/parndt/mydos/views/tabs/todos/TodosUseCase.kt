package de.parndt.mydos.views.tabs.todos

import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.repository.SettingsRepository
import de.parndt.mydos.repository.TodoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodosUseCase @Inject constructor(
    private var todoRepository: TodoRepository,
    private var settingsRepository: SettingsRepository
) {

    fun getAllTodos(): List<TodoEntity> {

        var listOfTodos = todoRepository.getAllTodos().sortedBy { it.dateCreated }.reversed()

        val settingFilterOnlyUnchecked =
            settingsRepository.getSetting(SettingsRepository.Settings.FILTER_ONLY_UNCHECKED).value
        val settingFilterOnlyPriority =
            settingsRepository.getSetting(SettingsRepository.Settings.FILTER_BY_PRIORITY).value
        val settingFilterByDate =
            settingsRepository.getSetting(SettingsRepository.Settings.FILTER_BY_DATE).value


        if (settingFilterOnlyUnchecked)
            listOfTodos = listOfTodos.filter { !it.done }

        if (settingFilterByDate)
            listOfTodos = listOfTodos.sortedBy { it.dateCreated }.reversed()

        if (settingFilterOnlyPriority)
            listOfTodos = listOfTodos.sortedBy { TodoPriority.valueOf(it.priority) }

        return listOfTodos

    }

    suspend fun updateSettingWithKey(settingKey: SettingsRepository.Settings, value: Boolean) {
        settingsRepository.updateSetting(settingKey, value)
    }

    suspend fun updateTodoEntry(todoId: Int, newStatus: Boolean) =
        todoRepository.updateTodo(todoId, newStatus)

    fun deleteTodoEntry(todo: TodoEntity) = todoRepository.removeTodoEntry(todo)
}
package de.parndt.mydos.views.tabs.todos

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

    suspend fun getAllTodos() = todoRepository.getAllTodos()

    suspend fun updateSettingWithKey(settingKey: SettingsRepository.Settings, value: Boolean) {
        settingsRepository.updateSetting(settingKey, value)
    }

    suspend fun updateTodoEntry(todoId: Int, newStatus: Boolean) =
        todoRepository.updateTodo(todoId, newStatus)

    fun deleteTodoEntry(todo: TodoEntity) = todoRepository.removeTodoEntry(todo)
}
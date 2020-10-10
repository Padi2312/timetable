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
        val settingFilterOnlyUnchecked =
            settingsRepository.getSetting(SettingsRepository.Filter.FILTER_ONLY_UNCHECKED).value

        if (settingFilterOnlyUnchecked)
            listOfTodos = listOfTodos.filter { !it.done }

        return listOfTodos.toMutableList()
    }

    suspend fun updateSettingWithKey(
        settingKey: SettingsRepository.Filter,
        value: Boolean,
        settingsUpdated: () -> Unit = {}
    ) {
        settingsRepository.updateSetting(settingKey, value, settingsUpdated)
    }

    suspend fun updateTodoEntry(todoId: Int, newStatus: Boolean) =
        todoRepository.updateTodoStatus(todoId, newStatus)

    suspend fun deleteTodoEntry(todo: TodoEntity) = todoRepository.removeTodoEntry(todo)

    suspend fun undoDeleteTodo(todo: TodoEntity) {
        todoRepository.updateDeletedFlag(todo.id,false)
    }

}
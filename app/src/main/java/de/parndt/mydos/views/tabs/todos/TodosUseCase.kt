package de.parndt.mydos.views.tabs.todos

import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.repository.TodoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodosUseCase @Inject constructor(
    private var todoRepository: TodoRepository
) {

    suspend fun getAllTodos() = todoRepository.getAllTodos()

    suspend fun updateTodoEntry(todoId: Int, newStatus: Boolean) =
        todoRepository.updateTodo(todoId, newStatus)

    suspend fun deleteTodoEntry(todo: TodoEntity) = todoRepository.removeTodoEntry(todo)
}
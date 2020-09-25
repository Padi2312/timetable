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

    suspend fun addNewTodoEntry(
        title: String,
        content: String? = null,
        priority: TodoPriority = TodoPriority.DEFAULT
    ): List<TodoEntity> {
        var todo = TodoEntity(title = title, content = content, priority = priority.name)
        if (todoRepository.createNewTodo(todo) != null)
            return getAllTodos()
        else
            return listOf()
    }

    suspend fun updateTodoEntry(todoId: Int, newStatus: Boolean) =
        todoRepository.updateTodo(todoId, newStatus)

    suspend fun deleteTodoEntry(todo: TodoEntity) = todoRepository.removeTodoEntry(todo)
}
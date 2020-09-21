package de.parndt.mydos.views.tabs.todos

import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.repository.TodoRepository
import javax.inject.Inject

class TodosUseCase @Inject constructor(
    private var todoRepository: TodoRepository) {

    fun getAllTodos(): List<TodoEntity> = todoRepository.getAllTodos()

    suspend fun addNewTodoEntry(
        title: String,
        content: String? = null,
        priority: TodoPriority = TodoPriority.DEFAULT
    ): Long {
        var todo = TodoEntity(title = title, content = content, priority = priority.name)
        return todoRepository.createNewTodo(todo)
    }

    suspend fun updateTodoEntry(todoId: Int, newStatus: Boolean) = todoRepository.updateTodo(todoId,newStatus)
}
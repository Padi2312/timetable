package de.parndt.mydos.views.tabs.home

import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.repository.TodoRepository
import javax.inject.Inject

class HomeUseCase @Inject constructor(private var todoRepository: TodoRepository) {

    suspend fun createTodoEntry(title:String,content:String? = null,priority: TodoPriority = TodoPriority.DEFAULT):Long{
        var todo = TodoEntity(title = title,content = content,priority = priority.name)
        return todoRepository.createNewTodo(todo)
    }

}
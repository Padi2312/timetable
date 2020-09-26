package de.parndt.mydos.utils.dialogs.newtodo

import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.repository.TodoRepository
import de.parndt.mydos.views.tabs.home.HomeUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewTodoDialogUseCase @Inject constructor(private var todoRepository: TodoRepository) {

    fun createTodoEntry(
        title: String,
        content: String,
        priority: TodoPriority = TodoPriority.DEFAULT
    ) {
        GlobalScope.launch {
            val todo = TodoEntity(title = title, content = content, priority = priority.name)
            todoRepository.createNewTodo(todo)
        }
    }


}
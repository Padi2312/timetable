package de.parndt.mydos.ui.customcomponent.newtododialog

import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.repository.TodoRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewTodoDialogUseCase @Inject constructor(private var todoRepository: TodoRepository) {

    fun createTodoEntry(
        title: String,
        content: String? = null,
        priority: TodoPriority = TodoPriority.DEFAULT,
        executionDate: String? = null
    ) {
        GlobalScope.launch {
            val todo = TodoEntity(
                title = title,
                content = content,
                priority = priority.name,
                executionDate = executionDate
            )
            todoRepository.createNewTodo(todo)
        }
    }


}
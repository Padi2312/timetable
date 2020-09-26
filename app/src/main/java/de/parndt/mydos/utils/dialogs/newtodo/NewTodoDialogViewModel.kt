package de.parndt.mydos.utils.dialogs.newtodo

import androidx.lifecycle.ViewModel
import de.parndt.mydos.database.models.todo.TodoPriority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewTodoDialogViewModel @Inject constructor(private var useCase: NewTodoDialogUseCase) :
    ViewModel() {


    fun createTodoEntry(
        title: String,
        content: String,
        priority: TodoPriority = TodoPriority.DEFAULT
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            useCase.createTodoEntry(title, content, priority)
        }
    }


}
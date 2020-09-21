package de.parndt.mydos.views.tabs.todos


import android.app.AlertDialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class TodosViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var useCase: TodosUseCase

    private var _todoList: MutableLiveData<List<TodoEntity>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            var list = useCase.getAllTodos()
            _todoList.postValue(list)
        }
    }

    fun getAllTodos() = _todoList


    fun createTodoEntry(
        title: String,
        content: String,
        priority: TodoPriority = TodoPriority.DEFAULT
    ): Long {

        return runBlocking {
            useCase.addNewTodoEntry(title, content, priority)
        }
    }

    fun updateTodoEntryStatus(todoId:Int,newStatus:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            useCase.updateTodoEntry(todoId,newStatus)
        }
    }
}

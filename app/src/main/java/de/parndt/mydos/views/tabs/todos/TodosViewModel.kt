package de.parndt.mydos.views.tabs.todos


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class TodosViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var useCase: TodosUseCase


    private var _todoList: MutableLiveData<MutableList<TodoEntity>> = MutableLiveData()

    fun observeNewTodos() = useCase.observeNewTodos()

    fun getAllTodos() = _todoList


    fun refreshTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            val todos = useCase.getAllTodos()
            _todoList.postValue(todos)
        }
    }

    fun filterTodosByDate() {
        viewModelScope.launch(Dispatchers.IO) {
            val todos = useCase.getAllTodos()
            todos.sortBy { it.dateCreated }
            todos.reverse()
            _todoList.postValue(todos)
        }
    }

    fun filterTodosByPriority() {
        viewModelScope.launch(Dispatchers.IO) {
            val todos = useCase.getAllTodos()
            todos.sortBy { TodoPriority.valueOf(it.priority) }
            _todoList.postValue(todos)
        }
    }

    fun updateTodoEntryStatus(todoId: Int, newStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.updateTodoEntry(todoId, newStatus)
            refreshTodoList()
        }
    }

    fun deleteTodoEntry(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.deleteTodoEntry(todo)
            refreshTodoList()
        }
    }

    fun updateFilterWithKey(filterKey: SettingsRepository.Filter, value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.updateSettingWithKey(filterKey, value) {
                refreshTodoList()
            }

        }
    }

    fun undoDeleteTodo(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.undoDeleteTodo(todo)
            refreshTodoList()
        }
    }


}

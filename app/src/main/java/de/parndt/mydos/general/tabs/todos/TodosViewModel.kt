package de.parndt.mydos.general.tabs.todos


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.general.tabs.settings.SettingsUseCase
import de.parndt.mydos.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class TodosViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var _context: Context

    @Inject
    lateinit var useCase: TodosUseCase

    @Inject
    lateinit var settingsUseCase: SettingsUseCase


    private var _todoList: MutableLiveData<MutableList<TodoEntity>> = MutableLiveData()

    fun observeNewTodos() = useCase.observeNewTodos()

    fun getAllTodos() = _todoList


    fun refreshTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            val todos = useCase.getAllTodos()
            _todoList.postValue(todos)
        }
    }

    fun filterTodosByDateCreated() {
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


    fun filterTodosByExecutionDate() {
        viewModelScope.launch(Dispatchers.IO) {
            val todos = useCase.getAllTodos()
            val sortedList =
                todos.sortedWith(compareBy({ it.executionDate }, { it.executionTime }))
                    .toMutableList()
            sortedList.reverse()
            _todoList.postValue(sortedList)
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

    fun deleteTodoEntry(todoId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val todo = useCase.getTodoById(todoId)
            useCase.deleteTodoEntry(todo)
            refreshTodoList()
        }
    }

    fun isDeleteOnCheckEnabled(): Boolean {
        return settingsUseCase.getSettingForKey(SettingsRepository.Settings.DELETE_ONCHECK).value
    }

    fun updateFilterWithKey(filterKey: SettingsRepository.Settings, value: Boolean) {
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

    fun getSortingFromItem(selectedItem: String): Filter {
        return when (selectedItem) {
            _context.getString(R.string.todos_sort_by_date_created) -> Filter.FILTER_BY_DATE_CREATED
            _context.getString(R.string.todos_sort_by_execution_date) -> Filter.FILTER_BY_EXECUTION_DATE
            _context.getString(R.string.todos_sort_by_priority) -> Filter.FILTER_BY_PRIORITY
            else -> Filter.FILTER_BY_DATE_CREATED
        }
    }

    enum class Filter {
        FILTER_BY_PRIORITY,
        FILTER_BY_EXECUTION_DATE,
        FILTER_BY_DATE_CREATED
    }
}

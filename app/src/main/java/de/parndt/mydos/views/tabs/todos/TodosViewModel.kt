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

    @Inject
    lateinit var settings: SettingsRepository

    private var _todoList: MutableLiveData<List<TodoEntity>> = MutableLiveData()

    fun getAllTodos() = _todoList


    fun refreshTodoList() {
        GlobalScope.launch {
            settings.getSetting(SettingsRepository.Settings.FILTER_ONLY_CHECKED)
            val todos = useCase.getAllTodos()
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
}

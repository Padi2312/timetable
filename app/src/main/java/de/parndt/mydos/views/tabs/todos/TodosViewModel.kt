package de.parndt.mydos.views.tabs.todos


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.parndt.mydos.database.models.settings.SettingsEntity
import de.parndt.mydos.database.models.todo.TodoEntity
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

    private var _settingsLiveData: MutableLiveData<SettingsEntity> = MutableLiveData()

    fun getAllTodos() = _todoList

    fun observeSettings() = _settingsLiveData

    fun refreshTodoList() {
        GlobalScope.launch {
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

    fun updateSettingWithKey(settingsKey: SettingsRepository.Settings, value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.updateSettingWithKey(settingsKey, value)
            refreshTodoList()
        }
    }


    fun initObserveSettings() {
        GlobalScope.launch {
            _settingsLiveData.postValue(settings.getSetting(SettingsRepository.Settings.FILTER_ONLY_UNCHECKED))
            _settingsLiveData.postValue(settings.getSetting(SettingsRepository.Settings.FILTER_BY_PRIORITY))
            _settingsLiveData.postValue(settings.getSetting(SettingsRepository.Settings.FILTER_BY_DATE))
        }
    }
}

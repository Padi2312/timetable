package de.parndt.mydos.views.tabs.home

import androidx.lifecycle.ViewModel
import de.parndt.mydos.database.models.todo.TodoPriority
import kotlinx.coroutines.*
import javax.inject.Inject


class HomeViewModel @Inject constructor() : ViewModel() {


    @Inject
    lateinit var homeUsecase: HomeUseCase

    fun createTodoEntry(
        title: String,
        content: String,
        priority: TodoPriority = TodoPriority.DEFAULT
    ): Long {

        return runBlocking {
            homeUsecase.createTodoEntry(title, content, priority)
        }
    }


}
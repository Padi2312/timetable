package de.parndt.mydos.ui.customcomponent.newtododialog

import android.content.Context
import androidx.lifecycle.ViewModel
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.database.models.todo.getString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewTodoDialogViewModel @Inject constructor(
    private var useCase: NewTodoDialogUseCase,
    private var _context: Context
) :
    ViewModel() {

    private var date: String? = null
    fun setDate(value: String?) = apply { date = value }
    private var time: String? = null
    fun setTime(value: String?) = apply { time = value }

    fun createTodoEntry(
        title: String,
        content: String? = null,
        priority: TodoPriority = TodoPriority.DEFAULT,
        executionDate: String? = null
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            useCase.createTodoEntry(title, content, priority, executionDate)
        }
    }


    fun getPriorityStringByValue(value: Int): String {
        return "${_context.getString(R.string.new_todo_priority)}: ${
            TodoPriority.fromInt(value).getString(_context)
        }"
    }

    fun getFormatedDateTime(): String? {
        return if (time == null)
            "$date"
        else if (date != null && time != null)
            "$date - $time Uhr"
        else
            null

    }


}
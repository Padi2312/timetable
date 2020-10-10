package de.parndt.mydos.ui.customcomponent.newtododialog

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
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
    fun getDate() = date
    private var time: String? = null
    fun setTime(value: String?) = apply { time = value }
    fun getTime() = time

    fun createTodoEntry(
        title: String,
        content: String? = null,
        priority: TodoPriority = TodoPriority.DEFAULT,
        executionDate: String? = null,
        executionTime: String? = null
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            useCase.createTodoEntry(title, content, priority, executionDate, executionTime)
        }
    }


    fun getPriorityStringByValue(value: Int): String {
        return "${_context.getString(R.string.new_todo_priority)}: ${
            TodoPriority.fromInt(value).getString(_context)
        }"
    }

    fun closeKeyboard(view: View) {
        val inputMethodManager =
            _context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
package de.parndt.mydos.ui.customcomponent.newtododialog

import android.app.Activity
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModel
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.notification.NotificationAlarmManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class NewTodoDialogViewModel @Inject constructor(
    private var useCase: NewTodoDialogUseCase,
    private var _context: Context,
    private var notificationAlarmManager: NotificationAlarmManager
) :
    ViewModel() {

    private var date: String? = null
    fun setDate(value: String?) = apply { date = value }
    fun getDate() = date
    private var time: String? = null
    fun setTime(value: String?) = apply { time = value }
    fun getTime() = time

    private var priority: TodoPriority = TodoPriority.DEFAULT
    fun setPriority(value: Int) = apply { priority = TodoPriority.fromInt(value) }

    private var content: String? = null

    private var enableNotification: Boolean = false
    fun getEnableNotification() = enableNotification
    fun setEnableNotification(value: Boolean) = apply { enableNotification = value }


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

    fun closeKeyboard(view: View) {
        val inputMethodManager =
            _context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun addNotificationAlarm(title: String, content: String?) {
        var calendar = Calendar.getInstance()
        if (getTime() != null && getDate() != null) {
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN)
            calendar.time = sdf.parse("${getDate()} ${getTime()}")
        } else if (getDate() != null && getTime() == null) {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
            calendar.time = sdf.parse("${getDate()}")
        } else {
            calendar = null
        }

        if (enableNotification && calendar != null)
            notificationAlarmManager.startAlarm(
                calendar,
                title,
                content ?: ""
            )
    }

    fun setContent(value: String?) {
        content = if (value.isNullOrEmpty())
            null
        else
            value
    }

    fun hasTodoChanges(): Boolean {
        return date != null || time != null || content != null || priority != TodoPriority.DEFAULT
    }

    fun getFormatedDate(date: String?): String {
        setDate(date)
        return if (date != null) {
            "${_context.getString(R.string.new_todo_date_label)} $date"
        } else
            _context.getString(R.string.new_todo_execution_date)
    }

    fun getFormatedDateTime(date: String?, time: String?): String {
        setDate(date)
        setTime(time)
        return if (date != null && time != null) {
            "${_context.getString(R.string.new_todo_date_label)} $date - $time Uhr"
        } else if (date != null && time == null) {
            "${_context.getString(R.string.new_todo_date_label)} $date"
        } else {
            _context.getString(R.string.new_todo_execution_date)
        }
    }

}
package de.parndt.mydos.ui.customcomponent.dialogs.newtodo

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.ViewModel
import de.parndt.mydos.database.models.todo.TodoPriority
import kotlinx.android.synthetic.main.dialog_new_todo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class NewTodoDialogViewModel @Inject constructor(private var useCase: NewTodoDialogUseCase) :
    ViewModel() {

    private var isExcutionDateTimeExpanded: Boolean = false
    private var isContentLayoutExpanded: Boolean = false


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

    fun isExecutionDateTimeExpanded(): Boolean {
        isExcutionDateTimeExpanded = !isExcutionDateTimeExpanded
        return isExcutionDateTimeExpanded
    }

    fun isContentLAyoutExpanded(): Boolean {
        isContentLayoutExpanded = !isContentLayoutExpanded
        return isContentLayoutExpanded
    }

    fun formatExecutionDate(dd: Int, MM: Int, yyyy: Int): String {
        return "${dd}.${MM}.${yyyy}"
    }

    fun formatExecutionTime(hh: Int, mm: Int): String {
        return "${hh}:${mm}"
    }

    fun getExecutionDate(
        dateEnabled: Boolean,
        timeEnabled: Boolean,
        datePicker: DatePicker,
        timePicker: TimePicker
    ): String? {

        var date = ""
        var time = ""

        if (dateEnabled)
            date = formatExecutionDate(
                datePicker.dayOfMonth,
                datePicker.month,
                datePicker.year
            )

        if (timeEnabled)
            time = formatExecutionTime(
                timePicker.hour,
                timePicker.minute
            )


        return if (date.isNotEmpty()) {
            date
        } else if (date.isNotEmpty() && time.isNotEmpty()) {
            "$date $time"
        } else
            null
    }
}
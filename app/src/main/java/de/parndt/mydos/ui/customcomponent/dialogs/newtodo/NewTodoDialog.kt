package de.parndt.mydos.ui.customcomponent.dialogs.newtodo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker

import androidx.fragment.app.DialogFragment
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.database.models.todo.getString
import kotlinx.android.synthetic.main.dialog_new_todo.*
import javax.inject.Inject

interface NewTodoDialogResult {
    fun addedEntry(): Unit
}

class NewTodoDialog(private var callback: NewTodoDialogResult) : DialogFragment() {

    @Inject
    lateinit var viewModel: NewTodoDialogViewModel

    @Inject
    lateinit var appcontext: Context

    companion object {
        fun newInstance(callback: NewTodoDialogResult): NewTodoDialog {
            return NewTodoDialog(callback)
        }
    }

    override fun onStart() {
        super.onStart()
        var dialog = dialog
        if (dialog != null)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_new_todo, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newtodoPriorityLabel.text =
            TodoPriority.fromInt(newtodoSliderPriority.value.toInt()).getString(appcontext)

        newtodoInputTitle.setOnFocusChangeListener { v, hasFocus ->
            updateShowErrorOnTitleView(hasFocus)
        }

        newTodoButtonAddContent.setOnClickListener { updateShowContentLayout() }
        newTodoSwitchExecutionDateTime.setOnClickListener { updateShowExecutionDateTimeLayout() }

        newTodoSwitchExecutionDate.setOnCheckedChangeListener { buttonView, isChecked ->
            updateShowExecutionDate(isChecked)
        }

        newTodoSwitchExecutionTime.setOnCheckedChangeListener { buttonView, isChecked ->
            updateShowExecutionTime(isChecked)
        }

        newtodoSliderPriority.addOnChangeListener { slider, value, fromUser ->
            setLabelForPrioritySlider(value)
        }

        newTodoButtonNewTodo.setOnClickListener {
            addNewTodo()
        }

        newTodoButtonCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun updateShowExecutionDate(isChecked: Boolean) {
        if (isChecked) {
            newTodoExecutionDatePicker.visibility = View.VISIBLE
            newTodoSwitchExecutionTime.isEnabled = true
        } else {
            newTodoExecutionDatePicker.visibility = View.GONE
            newTodoExecutionTimePicker.visibility = View.GONE
            newTodoSwitchExecutionTime.isChecked = false
            newTodoSwitchExecutionTime.isEnabled = false
        }
    }

    private fun updateShowExecutionTime(isChecked: Boolean) {
        if (isChecked) {
            newTodoExecutionTimePicker.visibility = View.VISIBLE
            newTodoSwitchExecutionDate.isChecked = true
        } else
            newTodoExecutionTimePicker.visibility = View.GONE
    }

    private fun updateShowExecutionDateTimeLayout() {
        if (viewModel.isExecutionDateTimeExpanded())
            newTodoExecutionDateTimeLayout.visibility = View.VISIBLE
        else
            newTodoExecutionDateTimeLayout.visibility = View.GONE
    }

    private fun updateShowContentLayout() {
        if (viewModel.isContentLAyoutExpanded())
            newTodoInputContentWrapper.visibility = View.VISIBLE
        else
            newTodoInputContentWrapper.visibility = View.GONE
    }

    private fun updateShowErrorOnTitleView(hasFocus: Boolean) {
        if (newtodoInputTitle.text?.isEmpty()!! && !hasFocus)
            showErrorTitleOnNewTodoPopUp(dialog)
        else
            hideErrorTitleOnNewTodoPopUp(dialog)
    }

    private fun setLabelForPrioritySlider(value: Float) {
        newtodoPriorityLabel.text =
            TodoPriority.fromInt(value.toInt()).getString(appcontext)
    }

    private fun addNewTodo() {
        if (newtodoInputTitle.text?.isEmpty()!!) {
            showErrorTitleOnNewTodoPopUp(dialog)
        } else {
            var content: String? = newTodoInputContent.text.toString()
            if (content!!.isEmpty())
                content = null

            viewModel.createTodoEntry(
                newtodoInputTitle.text.toString(),
                content,
                TodoPriority.fromInt(newtodoSliderPriority.value.toInt()),
                viewModel.getExecutionDate(
                    newTodoSwitchExecutionDate.isChecked,
                    newTodoSwitchExecutionTime.isChecked,
                    newTodoExecutionDatePicker,
                    newTodoExecutionTimePicker
                )
            )
            callback.addedEntry()
            dialog?.dismiss()
        }
    }


    private fun showErrorTitleOnNewTodoPopUp(dialog: Dialog?) {
        dialog?.newtodoInputTitleWrapper?.error =
            context?.getString(R.string.new_todo_title_error)
    }


    private fun hideErrorTitleOnNewTodoPopUp(dialog: Dialog?) {
        dialog?.newtodoInputTitleWrapper?.error = ""
    }
}
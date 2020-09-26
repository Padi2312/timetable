package de.parndt.mydos.utils.dialogs.newtodo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
            if (newtodoInputTitle.text?.isEmpty()!! && !hasFocus)
                showErrorTitleOnNewTodoPopUp(dialog)
            else
                hideErrorTitleOnNewTodoPopUp(dialog)
        }

        newtodoSwitchAddContent.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                newtodoInputContentWrapper.visibility = View.VISIBLE
            else
                newtodoInputContentWrapper.visibility = View.GONE
        }

        newtodoSliderPriority.addOnChangeListener { slider, value, fromUser ->
            newtodoPriorityLabel.text =
                TodoPriority.fromInt(value.toInt()).getString(appcontext)
        }

        newtodoButtonNewTodo.setOnClickListener {
            if (newtodoInputTitle.text?.isEmpty()!!) {
                showErrorTitleOnNewTodoPopUp(dialog)
            } else {
                viewModel.createTodoEntry(
                    newtodoInputTitle.text.toString(),
                    newtodoInputContent.text.toString(),
                    TodoPriority.fromInt(newtodoSliderPriority.value.toInt())
                )
                callback.addedEntry()
                dialog?.dismiss()
            }
        }

        newtodoButtonCancel.setOnClickListener {
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
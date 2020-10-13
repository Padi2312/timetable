package de.parndt.mydos.ui.customcomponent.newtododialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.notification.NotificationAlarmManager
import de.parndt.mydos.ui.customcomponent.datetimeselection.DateTimeSelectionFragment
import kotlinx.android.synthetic.main.dialog_new_todo.*
import javax.inject.Inject


interface NewTodoDialogResult {
    fun addedEntry()
}

class NewTodoDialogFragment(private var callback: NewTodoDialogResult) : DialogFragment(),
    DateTimeSelectionFragment.DateTimeSelectionCallbacks {

    @Inject
    lateinit var viewModel: NewTodoDialogViewModel

    @Inject
    lateinit var notificationAlarmManager: NotificationAlarmManager

    @Inject
    lateinit var _context: Context

    private lateinit var dateTimeSelectionFragment: DateTimeSelectionFragment

    companion object {
        fun newInstance(callback: NewTodoDialogResult): NewTodoDialogFragment {
            return NewTodoDialogFragment(callback)
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null)
            dialog!!.window!!.setLayout(
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

        setDateTimeSelection()


        viewModel.setEnableNotification(false)

        newtodoInputTitle.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
                viewModel.setContent(newtodoInputTitle.text.toString())
            updateShowErrorOnTitleView(
                hasFocus
            )
        }
        newTodoExpandContent.setOnClickListener { updateShowContentLayout() }
        newTodoExpandDateTimeSelection.setOnClickListener {
            updateShowExecutionDateTimeLayout()
            viewModel.closeKeyboard(view)
        }

        newTodoExpandPriority.setOnClickListener {
            updatePriorityLayout()
            viewModel.closeKeyboard(view)
        }

        newtodoSliderPriority.addOnChangeListener { _, value, _ ->
            onPrioritySliderChanged(value.toInt())
        }

        newTodoButtonNewTodo.setOnClickListener {
            addNewTodo()
        }

        newTodoButtonCancel.setOnClickListener {
            onClickCancelButton()
        }
    }

    private fun setDateTimeSelection() {
        val transaction = childFragmentManager.beginTransaction()
        dateTimeSelectionFragment = DateTimeSelectionFragment().setCallbackInterface(this)
        transaction.replace(R.id.newTodoDateTimeSelection, dateTimeSelectionFragment)
        transaction.commit()

    }

    private fun onClickCancelButton() {
        if (viewModel.hasTodoChanges()) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_todo_hasChanges_title)
                .setMessage(R.string.new_todo_hasChanges_message)
                .setPositiveButton(R.string.new_todo_hasChanges_positive_button) { _, _ ->
                    dialog?.dismiss()
                }
                .setNegativeButton(R.string.new_todo_hasChanges_negative_button, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        } else {
            dialog?.dismiss()
        }
    }

    private fun changeVisibilityOfElement(@IdRes elementToChangeVisibility: Int) {
        when (elementToChangeVisibility) {
            R.id.newtodoSliderPriority -> {
                newtodoSliderPriority.visibility = View.VISIBLE
                newTodoExpandPriority.setImageResource(R.drawable.ic_baseline_priority_high_24_selected)
                newTodoInputContentWrapper.visibility = View.GONE
                newTodoExpandContent.setImageResource(R.drawable.ic_baseline_description_24)
                newTodoExecutionDateTimeLayout.visibility = View.GONE
                newTodoExpandDateTimeSelection.setImageResource(R.drawable.ic_baseline_access_time_24)
            }
            R.id.newTodoExecutionDateTimeLayout -> {
                newTodoExecutionDateTimeLayout.visibility = View.VISIBLE
                newTodoExpandDateTimeSelection.setImageResource(R.drawable.ic_baseline_access_time_24_selected)
                newTodoInputContentWrapper.visibility = View.GONE
                newTodoExpandContent.setImageResource(R.drawable.ic_baseline_description_24)
                newtodoSliderPriority.visibility = View.GONE
                newTodoExpandPriority.setImageResource(R.drawable.ic_baseline_priority_high_24)
            }
            R.id.newTodoInputContentWrapper -> {
                newTodoInputContentWrapper.visibility = View.VISIBLE
                newTodoExpandContent.setImageResource(R.drawable.ic_baseline_description_24_selected)
                newTodoExecutionDateTimeLayout.visibility = View.GONE
                newTodoExpandDateTimeSelection.setImageResource(R.drawable.ic_baseline_access_time_24)
                newtodoSliderPriority.visibility = View.GONE
                newTodoExpandPriority.setImageResource(R.drawable.ic_baseline_priority_high_24)
            }
        }
    }

    private fun updatePriorityLayout() {
        if (newtodoSliderPriority.visibility == View.GONE) {
            changeVisibilityOfElement(R.id.newtodoSliderPriority)
        } else {
            newtodoSliderPriority.visibility = View.GONE
            newTodoExpandPriority.setImageResource(R.drawable.ic_baseline_priority_high_24)
        }
    }

    private fun updateShowExecutionDateTimeLayout() {
        if (newTodoExecutionDateTimeLayout.visibility == View.GONE) {
            changeVisibilityOfElement(R.id.newTodoExecutionDateTimeLayout)
        } else {
            newTodoExecutionDateTimeLayout.visibility = View.GONE
            newTodoExpandDateTimeSelection.setImageResource(R.drawable.ic_baseline_access_time_24)
        }
    }

    private fun updateShowContentLayout() {
        if (newTodoInputContentWrapper.visibility == View.GONE) {
            changeVisibilityOfElement(R.id.newTodoInputContentWrapper)
        } else {
            newTodoInputContentWrapper.visibility = View.GONE
            newTodoExpandContent.setImageResource(R.drawable.ic_baseline_description_24)
        }
    }


    private fun updateShowErrorOnTitleView(hasFocus: Boolean) {
        if (newtodoInputTitle.text?.isEmpty()!! && !hasFocus)
            showErrorTitleOnNewTodoPopUp(dialog)
        else
            hideErrorTitleOnNewTodoPopUp(dialog)
    }

    private fun onPrioritySliderChanged(value: Int) {
        viewModel.setPriority(value)
    }

    private fun addNewTodo() {
        if (newtodoInputTitle.text?.isEmpty()!!) {
            showErrorTitleOnNewTodoPopUp(dialog)
        } else {
            val title = newtodoInputTitle.text.toString()
            var content: String? = newTodoInputContent.text.toString()
            if (content!!.isEmpty())
                content = null

            viewModel.createTodoEntry(
                title = title,
                content = content,
                priority = TodoPriority.fromInt(newtodoSliderPriority.value.toInt()),
                executionDate = viewModel.getDate(),
                executionTime = viewModel.getTime()
            )

            if (viewModel.getEnableNotification())
                viewModel.addNotificationAlarm(title, content)

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

    override fun onFormatedDate(date: String?) {
        viewModel.setDate(date)
    }

    override fun onFormatedDateTime(date: String?, time: String?) {
        viewModel.setDate(date)
        viewModel.setTime(time)
    }

    override fun onSwitchNotificationChanged(isChecked: Boolean) {
        viewModel.setEnableNotification(isChecked)
    }


}

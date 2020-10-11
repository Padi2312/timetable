package de.parndt.mydos.ui.customcomponent.newtododialog

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
import de.parndt.mydos.extensions.setDrawableEndShowLess
import de.parndt.mydos.extensions.setDrawableEndShowMore
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

        newTodoExpandPriority.text =
            viewModel.getPriorityStringByValue(newtodoSliderPriority.value.toInt())

        viewModel.setEnableNotification(false)

        newtodoInputTitle.setOnFocusChangeListener { v, hasFocus ->
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
            setLabelForPrioritySlider(value.toInt())
        }

        newTodoButtonNewTodo.setOnClickListener {
            addNewTodo()
        }

        newTodoButtonCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun setDateTimeSelection() {
        val transaction = childFragmentManager.beginTransaction()
        dateTimeSelectionFragment = DateTimeSelectionFragment().setCallbackInterface(this)
        transaction.replace(R.id.newTodoDateTimeSelection, dateTimeSelectionFragment)
        transaction.commit()

    }

    private fun updatePriorityLayout() {
        if (newtodoSliderPriority.visibility == View.GONE) {
            newtodoSliderPriority.visibility = View.VISIBLE
            newTodoExpandPriority.setDrawableEndShowLess( _context)
        } else {
            newtodoSliderPriority.visibility = View.GONE
            newTodoExpandPriority.setDrawableEndShowMore( _context)
        }
    }


    private fun updateShowExecutionDateTimeLayout() {
        if (newTodoExecutionDateTimeLayout.visibility == View.GONE) {
            newTodoExecutionDateTimeLayout.visibility = View.VISIBLE
            newTodoExpandPriority.setDrawableEndShowLess( _context)
        } else {
            newTodoExecutionDateTimeLayout.visibility = View.GONE
            newTodoExpandDateTimeSelection.setDrawableEndShowMore( _context)
        }
    }

    private fun updateShowContentLayout() {
        if (newTodoInputContentWrapper.visibility == View.GONE) {
            newTodoInputContentWrapper.visibility = View.VISIBLE
            newTodoExpandContent.setDrawableEndShowLess( _context)
        } else {
            newTodoInputContentWrapper.visibility = View.GONE
            newTodoExpandContent.setDrawableEndShowMore( _context)
        }
    }

    private fun updateShowErrorOnTitleView(hasFocus: Boolean) {
        if (newtodoInputTitle.text?.isEmpty()!! && !hasFocus)
            showErrorTitleOnNewTodoPopUp(dialog)
        else
            hideErrorTitleOnNewTodoPopUp(dialog)
    }

    private fun setLabelForPrioritySlider(value: Int) {
        newTodoExpandPriority.text =
            viewModel.getPriorityStringByValue(value)
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
        newTodoExpandDateTimeSelection.text = viewModel.getFormatedDate(date)
    }

    override fun onFormatedDateTime(date: String?, time: String?) {
        newTodoExpandDateTimeSelection.text = viewModel.getFormatedDateTime(date, time)
    }

    override fun onSwitchNotificationChanged(isChecked: Boolean) {
        viewModel.setEnableNotification(isChecked)
    }


}
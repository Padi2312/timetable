package de.parndt.mydos.ui.customcomponent.newtododialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.tabs.TabLayout
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.database.models.todo.getDrawableResource
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
            updateShowErrorOnTitleView(
                hasFocus
            )
        }
        /*newTodoExpandContent.setOnClickListener { updateShowContentLayout() }
        newTodoExpandDateTimeSelection.setOnClickListener {
            updateShowExecutionDateTimeLayout()
            viewModel.closeKeyboard(view)
        }

        newTodoExpandPriority.setOnClickListener {
            updatePriorityLayout()
            viewModel.closeKeyboard(view)
        }*/

        newtodoSliderPriority.addOnChangeListener { _, value, _ ->
            setDrawableForPriorityButton(value.toInt())
        }

        newTodoButtonNewTodo.setOnClickListener {
            addNewTodo()
        }

        newTodoActionTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.icon) {
                    _context.getDrawable(R.drawable.ic_baseline_priority_high_24) -> {
                        updatePriorityLayout()
                        viewModel.closeKeyboard(view)
                        true
                    }
                    _context.getDrawable(R.drawable.ic_baseline_access_time_24) -> {
                        updateShowExecutionDateTimeLayout()
                        viewModel.closeKeyboard(view)
                        true
                    }
                    _context.getDrawable(R.drawable.ic_baseline_description_24) -> {
                        updateShowContentLayout()
                        true
                    }
                    else -> {
                        false
                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        newTodoButtonCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun setDrawableForPriorityButton(value: Int) {
        val priority = TodoPriority.fromInt(value)
        val drawable = priority.getDrawableResource()
        newTodoActionTabLayout.getTabAt(0)?.setIcon(drawable)
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
            newTodoExecutionDateTimeLayout.visibility = View.GONE
            newTodoInputContentWrapper.visibility = View.GONE
        } else {
            newtodoSliderPriority.visibility = View.GONE
        }
    }

    private fun updateShowExecutionDateTimeLayout() {
        if (newTodoExecutionDateTimeLayout.visibility == View.GONE) {
            newTodoExecutionDateTimeLayout.visibility = View.VISIBLE
            newtodoSliderPriority.visibility = View.GONE
            newTodoInputContentWrapper.visibility = View.GONE
        } else {
            newTodoExecutionDateTimeLayout.visibility = View.GONE
        }
    }

    private fun updateShowContentLayout() {
        if (newTodoInputContentWrapper.visibility == View.GONE) {
            newTodoInputContentWrapper.visibility = View.VISIBLE
            newTodoExecutionDateTimeLayout.visibility = View.GONE
            newtodoSliderPriority.visibility = View.GONE
        } else {
            newTodoInputContentWrapper.visibility = View.GONE
        }
    }

    private fun updateShowErrorOnTitleView(hasFocus: Boolean) {
        if (newtodoInputTitle.text?.isEmpty()!! && !hasFocus)
            showErrorTitleOnNewTodoPopUp(dialog)
        else
            hideErrorTitleOnNewTodoPopUp(dialog)
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
        viewModel.getFormatedDate(date)
    }

    override fun onFormatedDateTime(date: String?, time: String?) {
        viewModel.getFormatedDateTime(date, time)
    }

    override fun onSwitchNotificationChanged(isChecked: Boolean) {
        viewModel.setEnableNotification(isChecked)
    }


}
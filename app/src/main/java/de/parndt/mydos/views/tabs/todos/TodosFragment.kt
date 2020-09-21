package de.parndt.mydos.views.tabs.todos

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import kotlinx.android.synthetic.main.fragment_tab_todos.*
import kotlinx.android.synthetic.main.popup_new_todo.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import javax.inject.Inject

class TodosFragment : Fragment(), TodoOnCheck {

    @Inject
    lateinit var todosViewModel: TodosViewModel


    private lateinit var adapter: TodosListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_todos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TodosListAdapter(this)
        todos_todo_list.adapter = adapter
        todos_todo_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        todos_floating_action_button.setOnClickListener {
            openNewTodoPopUp()
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        todosViewModel.getAllTodos().observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun openNewTodoPopUp() {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.popup_new_todo)

        val dialog = builder.create()
        dialog.show()

        val titleView = dialog.findViewById<EditText>(R.id.newtodo_input_title)
        val contentView = dialog.findViewById<EditText>(R.id.newtodo_input_content)
        val contentText = contentView.text.toString()
        val btnAddTodo = dialog.findViewById<Button>(R.id.newtodo_btn_new_todo)
        val btnCancelAdd = dialog.findViewById<Button>(R.id.newtodo_btn_cancel)

        titleView.setOnFocusChangeListener { v, hasFocus ->
            if (titleView.text.isEmpty() && !hasFocus)
                showErrorTitleOnNewTodoPopUp(dialog)
            else
                hideErrorTitleOnNewTodoPopUp(dialog)
        }


        btnAddTodo.setOnClickListener {

            if (titleView.text.isEmpty()) {
                showErrorTitleOnNewTodoPopUp(dialog)
                return@setOnClickListener
            } else
                GlobalScope.launch {
                    todosViewModel.createTodoEntry(titleView.text.toString(), contentText)
                    dialog.dismiss()
                }
        }

        btnCancelAdd.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun showErrorTitleOnNewTodoPopUp(dialog: AlertDialog) {
        dialog.newtodo_input_title_wrapper.error = context?.getString(R.string.new_todo_title_error)
    }


    private fun hideErrorTitleOnNewTodoPopUp(dialog: AlertDialog) {
        dialog.newtodo_input_title_wrapper.error = ""
    }

    override fun onCheckboxClicked(todoId: Int, checked: Boolean) {
        todosViewModel.updateTodoEntryStatus(todoId, checked)
    }

}
package de.parndt.mydos.views.tabs.todos

import android.app.AlertDialog
import android.content.Context
import android.opengl.Visibility
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
import com.google.android.material.textview.MaterialTextView
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoEntity
import kotlinx.android.synthetic.main.tab_fragment_todos.*
import kotlinx.android.synthetic.main.dialog_new_todo.*
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
        return inflater.inflate(R.layout.tab_fragment_todos, container, false)
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

        todosViewModel.startObservingTodos()

        todosViewModel.getAllTodos().observe(this, Observer {
            if (it.isEmpty())
                todos_empty_todo_list_label.visibility = View.VISIBLE
            else
                todos_empty_todo_list_label.visibility = View.GONE

            adapter.submitList(it)
        })

    }

    private fun openNewTodoPopUp() {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_new_todo)

        val dialog = builder.create()
        dialog.show()

        val titleView = dialog.findViewById<EditText>(R.id.newtodo_input_title)
        val contentView = dialog.findViewById<EditText>(R.id.newtodo_input_content)
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
                    todosViewModel.createTodoEntry(
                        titleView.text.toString(),
                        contentView.text.toString()
                    )
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

    override fun onTodoItemClicked(todo: TodoEntity) {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_todo_item)

        val dialog = builder.create()
        dialog.show()

        val titleView = dialog.findViewById<MaterialTextView>(R.id.dialog_todo_item_title)
        val contentView = dialog.findViewById<MaterialTextView>(R.id.dialog_todo_item_content)

        titleView.text = todo.title
        if (todo.content.isNullOrEmpty())
            contentView.visibility = View.GONE
        else
            contentView.text = todo.content
    }

    override fun onTodoItemDeleteClicked(todo: TodoEntity) {
        todosViewModel.deleteTodoEntry(todo)
    }

}
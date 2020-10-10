package de.parndt.mydos.general.tabs.todos

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.repository.SettingsRepository
import de.parndt.mydos.ui.customcomponent.newtododialog.NewTodoDialogFragment
import de.parndt.mydos.ui.customcomponent.newtododialog.NewTodoDialogResult
import kotlinx.android.synthetic.main.tab_fragment_todos.*

import javax.inject.Inject

class TodosFragment : Fragment(), TodoOnCheck, NewTodoDialogResult {

    @Inject
    lateinit var viewModel: TodosViewModel

    @Inject
    lateinit var _context: Context

    private lateinit var adapter: TodosListAdapter

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_fragment_todos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TodosListAdapter(this, _context)
        todos_todo_list.adapter = adapter
        todos_todo_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        sortingTodosListener()

        todosSorting.adapter = ArrayAdapter.createFromResource(
            _context,
            R.array.todo_filter,
            R.layout.dropdown_list_item_todos_filter
        )
        todosSorting.setSelection(0)

        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(todos_todo_list)

        todos_floating_action_button.setOnClickListener {
            openNewTodoPopUp()
        }

    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        viewModel.refreshTodoList()


        viewModel.observeNewTodos().observe(this, Observer {
            viewModel.refreshTodoList()
        })

        viewModel.getAllTodos().observe(this, Observer {
            if (it.isEmpty())
                todos_empty_todo_list_label.visibility = View.VISIBLE
            else
                todos_empty_todo_list_label.visibility = View.GONE

            adapter.submitList(it)
        })

    }


    private fun sortingTodosListener() {

        todosSorting.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedFilter = when (todosSorting.selectedItem.toString()) {
                    _context.getString(R.string.todos_filter_by_date) -> SettingsRepository.Filter.FILTER_BY_DATE
                    _context.getString(R.string.todos_filter_by_priority) -> SettingsRepository.Filter.FILTER_BY_PRIORITY
                    else -> SettingsRepository.Filter.FILTER_ONLY_UNCHECKED
                }

                when (selectedFilter) {
                    SettingsRepository.Filter.FILTER_ONLY_UNCHECKED -> {}
                    SettingsRepository.Filter.FILTER_BY_PRIORITY -> viewModel.filterTodosByPriority()
                    SettingsRepository.Filter.FILTER_BY_DATE -> viewModel.filterTodosByDate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }


    private fun openNewTodoPopUp() {
        NewTodoDialogFragment.newInstance(this).show(parentFragmentManager, "dialog_new_todo")
    }

    val itemTouchHelperCallback =
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.adapterPosition
                val item = adapter.getItemByPosition(itemPosition)
                viewModel.deleteTodoEntry(item)

                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    R.string.todos_delete_snackbar_text,
                    Snackbar.LENGTH_SHORT
                ).setAction(R.string.global_undo) {
                    viewModel.undoDeleteTodo(item)
                }.show()
            }

        }


    override fun onCheckboxClicked(todoId: Int, checked: Boolean) {
        viewModel.updateTodoEntryStatus(todoId, checked)
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
        viewModel.deleteTodoEntry(todo)
    }

    override fun addedEntry() {
        viewModel.refreshTodoList()
    }

}
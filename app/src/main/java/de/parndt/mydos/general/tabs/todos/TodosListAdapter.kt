package de.parndt.mydos.general.tabs.todos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoEntity
import de.parndt.mydos.database.models.todo.TodoPriority
import de.parndt.mydos.database.models.todo.getDrawable
import kotlinx.android.synthetic.main.list_item_todo.view.*

interface TodoOnCheck {
    fun onCheckboxClicked(todoId: Int, checked: Boolean)
    fun onTodoItemClicked(todo: TodoEntity)
}

class TodosListAdapter(val todoOnCheck: TodoOnCheck, val _context: Context) :
    ListAdapter<TodoEntity, TodosListAdapter.TodosViewHolder>(TodosListDiffCallback) {

    private var todosList: MutableList<TodoEntity> = mutableListOf()

    inner class TodosViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(item: TodoEntity, _context: Context) {
            itemView.todo_item_title.text = item.title
            itemView.todo_item_checkbox.isChecked = item.done

            if (!item.executionDate.isNullOrEmpty() && !item.executionTime.isNullOrEmpty()) {
                itemView.todo_item_execution_date.visibility = View.VISIBLE
                itemView.todo_item_execution_date.text =
                    "${item.executionDate} - ${item.executionTime} Uhr"
            } else if (!item.executionDate.isNullOrEmpty()) {
                itemView.todo_item_execution_date.visibility = View.VISIBLE
                itemView.todo_item_execution_date.text = "${item.executionDate}"
            } else {
                itemView.todo_item_execution_date.visibility = View.GONE
            }

            itemView.todo_item_priority_icon.setImageDrawable(
                TodoPriority.valueOf(item.priority).getDrawable(_context)
            )

            itemView.todo_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                todoOnCheck.onCheckboxClicked(item.id, isChecked)
            }

            itemView.setOnClickListener {
                todoOnCheck.onTodoItemClicked(item)
            }
        }

    }

    fun getItemByPosition(position: Int): TodoEntity {
        return todosList[position]
    }

    override fun submitList(list: MutableList<TodoEntity>?) {
        super.submitList(list)
        todosList = list ?: mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosViewHolder {
        return TodosViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_todo, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodosViewHolder, position: Int) {
        holder.bind(getItem(position), _context)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}


object TodosListDiffCallback : DiffUtil.ItemCallback<TodoEntity>() {
    override fun areItemsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
        return oldItem.id == newItem.id
    }
}

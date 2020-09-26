package de.parndt.mydos.views.tabs.todos

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
import de.parndt.mydos.database.models.todo.getIcon
import kotlinx.android.synthetic.main.dialog_todo_item.view.*
import kotlinx.android.synthetic.main.list_item_todo.view.*

interface TodoOnCheck {
    fun onCheckboxClicked(todoId: Int, checked: Boolean)
    fun onTodoItemClicked(todo: TodoEntity)
    fun onTodoItemDeleteClicked(todo: TodoEntity)

}

class TodosListAdapter(val todoOnCheck: TodoOnCheck, val _context: Context) :
    ListAdapter<TodoEntity, TodosListAdapter.TodosViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<TodoEntity>() {
        override fun areItemsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem == newItem
        }
    }

    inner class TodosViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(item: TodoEntity, _context: Context) {
            itemView.todo_item_title.text = item.title
            itemView.todo_item_checkbox.isChecked = item.done

            itemView.todo_item_priority_icon.setImageDrawable(
                TodoPriority.valueOf(item.priority).getIcon(_context)
            )

            itemView.todo_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                todoOnCheck.onCheckboxClicked(item.id!!, isChecked)
            }

            itemView.setOnClickListener {
                todoOnCheck.onTodoItemClicked(item)
            }
        }

    }

    fun getItemByPosition(position: Int): TodoEntity {
        return getItem(position)
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


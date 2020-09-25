package de.parndt.mydos.views.tabs.todos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoEntity
import kotlinx.android.synthetic.main.dialog_todo_item.view.*
import kotlinx.android.synthetic.main.list_item_todo.view.*

interface TodoOnCheck {
    fun onCheckboxClicked(todoId: Int, checked: Boolean)
    fun onTodoItemClicked(todo: TodoEntity)
    fun onTodoItemDeleteClicked(todo: TodoEntity)

}

class TodosListAdapter(val todoOnCheck: TodoOnCheck) :
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

        fun bind(item: TodoEntity) {
            itemView.todo_item_checkbox.text = item.title
            itemView.todo_item_checkbox.isChecked = item.done

            itemView.todo_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                todoOnCheck.onCheckboxClicked(item.id!!, isChecked)
            }

            itemView.todo_item_delete.setOnClickListener {
                todoOnCheck.onTodoItemDeleteClicked(item)
            }

            itemView.setOnClickListener {
                todoOnCheck.onTodoItemClicked(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosViewHolder {
        return TodosViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_todo, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}


package de.parndt.mydos.views.tabs.todos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.parndt.mydos.R
import de.parndt.mydos.database.models.todo.TodoEntity
import kotlinx.android.synthetic.main.todo_list_item.view.*

interface TodoOnCheck {
    fun onCheckboxClicked(todoId: Int, checked: Boolean)
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

    inner class TodosViewHolder(view: View, todoOnCheck: TodoOnCheck) :
        RecyclerView.ViewHolder(view) {

        fun bind(item: TodoEntity) {
            itemView.todo_item_checkbox.text = item.title
            itemView.todo_item_checkbox.isChecked = item.done

            itemView.todo_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked)
                    todoOnCheck.onCheckboxClicked(item.id!!, isChecked)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosViewHolder {
        return TodosViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_list_item, parent, false),
            todoOnCheck
        )
    }

    override fun onBindViewHolder(holder: TodosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}


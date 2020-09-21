package de.parndt.mydos.utils

import androidx.recyclerview.widget.DiffUtil

class CustomListDiffCallback<T:DiffCallBackFunctions>: DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return  oldItem == newItem
    }

}

interface DiffCallBackFunctions {
    override operator fun equals(other: Any?): Boolean
}
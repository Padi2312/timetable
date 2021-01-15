package de.parndt.timetable.general.tabs.daily

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.parndt.timetable.R
import de.parndt.timetable.general.timetable.Lecture
import kotlinx.android.synthetic.main.list_item_lecture.view.*


class DailyLecutresListAdapter(val _context: Context) :
    ListAdapter<Lecture, DailyLecutresListAdapter.LecturesViewHolder>(LecturesListDiffCallback) {

    private var lecturesList: List<Lecture> = mutableListOf()

    inner class LecturesViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(item: Lecture, _context: Context) {
            itemView.lecutreName.text = item.name
            itemView.lecutreTime.text = item.time
        }

    }

    fun getItemByPosition(position: Int): Lecture {
        return lecturesList[position]
    }

    override fun submitList(list: List<Lecture>?) {
        super.submitList(list)
        lecturesList = list ?: mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturesViewHolder {
        return LecturesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_lecture, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LecturesViewHolder, position: Int) {
        holder.bind(getItem(position), _context)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}


object LecturesListDiffCallback : DiffUtil.ItemCallback<Lecture>() {
    override fun areItemsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
        return oldItem.time == newItem.time && oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
        return oldItem.time == newItem.time && oldItem.date == newItem.date && oldItem.name == newItem.name
    }
}

package de.parndt.timetable.general.tabs.weekly

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.parndt.timetable.general.tabs.weekly.viewholder.DayOfWeekViewHolder
import de.parndt.timetable.general.tabs.weekly.viewholder.WeeklyLectureViewHolder
import de.parndt.timetable.general.timetable.DayOfWeek
import de.parndt.timetable.general.timetable.WeeklyLecture
import de.parndt.timetable.general.timetable.WeeklyLecturesModelItem
import de.parndt.timetable.utils.Logger

const val dayOfWeekType = 0
const val weeklyLectureType = 1

class WeeklyLecutresListAdapter(val _context: Context) :
    ListAdapter<WeeklyLecturesModelItem, RecyclerView.ViewHolder>(WeeklyLecturesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            weeklyLectureType -> {
                WeeklyLectureViewHolder.from(parent)
            }
            dayOfWeekType -> {
                DayOfWeekViewHolder.from(parent)
            }
            else -> {
                Logger.error("No viewholder found")
                WeeklyLectureViewHolder.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is WeeklyLecture -> {
                (holder as WeeklyLectureViewHolder).bind(item)
            }
            is DayOfWeek -> {
                (holder as DayOfWeekViewHolder).bind(item)
            }
            else -> {
                Logger.error("No viewholder found for position: $position")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (super.getItem(position)) {
            is DayOfWeek -> dayOfWeekType
            is WeeklyLecture -> weeklyLectureType
            else -> {
                Logger.error("Wrong type for position $position")
                0
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}


object WeeklyLecturesDiffCallback : DiffUtil.ItemCallback<WeeklyLecturesModelItem>() {
    override fun areItemsTheSame(
        oldItem: WeeklyLecturesModelItem,
        newItem: WeeklyLecturesModelItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: WeeklyLecturesModelItem,
        newItem: WeeklyLecturesModelItem
    ): Boolean {
        return oldItem == newItem
    }
}

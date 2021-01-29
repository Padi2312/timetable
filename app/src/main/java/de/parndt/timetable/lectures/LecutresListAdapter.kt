package de.parndt.timetable.lectures

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.parndt.timetable.lectures.viewholder.CurrentLecturesDayViewHolder
import de.parndt.timetable.lectures.viewholder.DefaultLecturesDayViewHolder
import de.parndt.timetable.lectures.viewholder.PreviousLecturesDayViewHolder
import de.parndt.timetable.lectures.viewholder.WeekendDayViewHolder
import de.parndt.timetable.lecturesmodels.*
import de.parndt.timetable.utils.Logger
import java.time.LocalDate


const val currentLecturesDay = 0
const val previousLecturesDay = 1
const val weekendDay = 2
const val defaultDay = 3

class LecutresListAdapter(val _context: Context) :
        ListAdapter<LecturesDay, RecyclerView.ViewHolder>(LecturesListDiffCallback) {

    private var lecturesList: List<LecturesDay> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            currentLecturesDay -> {
                CurrentLecturesDayViewHolder.from(parent, _context)
            }
            previousLecturesDay -> {
                PreviousLecturesDayViewHolder.from(parent, _context)
            }
            weekendDay -> {
                WeekendDayViewHolder.from(parent, _context)
            }
            defaultDay -> {
                DefaultLecturesDayViewHolder.from(parent, _context)
            }
            else -> {
                Logger.error("No view type found for $viewType")
                DefaultLecturesDayViewHolder.from(parent, _context)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is CurrentLecturesDay -> {
                (holder as CurrentLecturesDayViewHolder).bind(item)
            }
            is PreviousLecturesDay -> {
                (holder as PreviousLecturesDayViewHolder).bind(item)
            }
            is Weekend -> {
                (holder as WeekendDayViewHolder).bind(item)
            }
            is DefaultLecturesDay -> {
                (holder as DefaultLecturesDayViewHolder).bind(item)
            }
            else -> {
                Logger.error("No view holder found for $item")
                (holder as DefaultLecturesDayViewHolder).bind(item)
            }
        }
    }

    override fun submitList(list: List<LecturesDay>?) {
        super.submitList(list)
        lecturesList = list ?: mutableListOf()
    }


    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return when (super.getItem(position)) {
            is CurrentLecturesDay -> {
                currentLecturesDay
            }
            is DefaultLecturesDay -> {
                defaultDay
            }
            is PreviousLecturesDay -> {
                previousLecturesDay
            }
            is Weekend -> {
                weekendDay
            }
        }
    }

    fun getPositionOfItemByDate(date: LocalDate): Int {
        return this.currentList.indexOfFirst { it.getDateValue() == date }
    }

}


object LecturesListDiffCallback : DiffUtil.ItemCallback<LecturesDay>() {
    override fun areItemsTheSame(oldItem: LecturesDay, newItem: LecturesDay): Boolean {
        return oldItem.getUUID() == newItem.getUUID()
    }

    override fun areContentsTheSame(oldItem: LecturesDay, newItem: LecturesDay): Boolean {

        return oldItem.getUUID() == newItem.getUUID()
    }
}

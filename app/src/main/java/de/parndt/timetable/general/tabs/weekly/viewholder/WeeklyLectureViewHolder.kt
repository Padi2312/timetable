package de.parndt.timetable.general.tabs.weekly.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.parndt.timetable.R
import de.parndt.timetable.general.timetable.WeeklyLecture
import kotlinx.android.synthetic.main.list_item_lecture.view.*

class WeeklyLectureViewHolder private constructor(view: View): RecyclerView.ViewHolder(view){

    fun bind(item: WeeklyLecture){
        itemView.lecutreName.text = item.lecture.name
        itemView.lecutreTime.text = item.lecture.time
    }

    companion object {
        fun from(parent: ViewGroup): WeeklyLectureViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_lecture, parent, false)

            return WeeklyLectureViewHolder(
                view
            )
        }
    }
}
package de.parndt.timetable.lectures.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.parndt.timetable.R
import de.parndt.timetable.lecturesmodels.Weekend
import kotlinx.android.synthetic.main.list_item_weekend_day.view.*

class WeekendDayViewHolder private constructor(view: View,private val context: Context): RecyclerView.ViewHolder(view){

    fun bind(item: Weekend){
        itemView.weekendLectureDate.text = item.getDate()
    }

    companion object {
        fun from(parent: ViewGroup,context: Context): WeekendDayViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_weekend_day, parent, false)

            return WeekendDayViewHolder(
                    view,
                    context
            )
        }
    }
}
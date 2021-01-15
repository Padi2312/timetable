package de.parndt.timetable.general.tabs.weekly.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.parndt.timetable.R
import de.parndt.timetable.general.timetable.DayOfWeek
import kotlinx.android.synthetic.main.list_item_dayofweek.view.*

class DayOfWeekViewHolder private constructor(view: View): RecyclerView.ViewHolder(view){

    fun bind(item: DayOfWeek){
        itemView.dayOfWeek.text = "${item.day}  ${item.date}"
    }

    companion object {
        fun from(parent: ViewGroup): DayOfWeekViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_dayofweek, parent, false)

            return DayOfWeekViewHolder(
                view
            )
        }
    }
}
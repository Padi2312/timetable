package de.parndt.timetable.general.lectures.viewholder

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.parndt.timetable.R
import de.parndt.timetable.lecturesmodels.LecturesDay
import kotlinx.android.synthetic.main.list_item_current_lectures_day.view.*
import kotlinx.android.synthetic.main.list_item_current_lectures_day.view.dailyLecturesView
import kotlinx.android.synthetic.main.list_item_default_lectures.view.*
import java.time.LocalDate

class CurrentLecturesDayViewHolder private constructor(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

    fun bind(item: LecturesDay) {
        itemView.currentLectureDate.text = item.getDate()

        itemView.currentLecturesOfDay?.removeAllViews()

        if (item.getDateValue() == LocalDate.now())
            itemView.dailyLecturesView.strokeWidth = 6


        val lecturesOfDay = item.getLecturesOfDay()

        for (i in lecturesOfDay.indices) {

            val lecturesView = LayoutInflater.from(context)
                    .inflate(R.layout.list_item_lecture, null, true)

            lecturesView.findViewById<TextView>(R.id.lectureName).text = lecturesOfDay[i].name
            lecturesView.findViewById<TextView>(R.id.lectureTime).text = lecturesOfDay[i].time

            itemView.currentLecturesOfDay.addView(lecturesView)

            if (i != lecturesOfDay.size - 1) {
                itemView.currentLecturesOfDay.addView(getSeperatorView())
            }
        }


    }

    private fun getSeperatorView(): View {
        val seperator = View(context)
        seperator.setBackgroundColor(Color.GRAY)
        seperator.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2)
        return seperator
    }

    companion object {
        fun from(parent: ViewGroup, context: Context): CurrentLecturesDayViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_current_lectures_day, parent, false)

            return CurrentLecturesDayViewHolder(
                    view,
                    context
            )
        }
    }
}

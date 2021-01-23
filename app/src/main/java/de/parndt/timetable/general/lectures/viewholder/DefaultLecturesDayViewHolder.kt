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
import kotlinx.android.synthetic.main.list_item_default_lectures.view.*

class DefaultLecturesDayViewHolder private constructor(view: View, private val context: Context): RecyclerView.ViewHolder(view){

    fun bind(item: LecturesDay){
        itemView.defaultLectureDate.text = item.getDate()

        if (itemView.defaultLecturesOfDay.childCount == 0) {

            val lecturesOfDay = item.getLecturesOfDay()

            for (i in lecturesOfDay.indices) {

                val lecturesView = LayoutInflater.from(context)
                        .inflate(R.layout.list_item_lecture, null, true)

                lecturesView.findViewById<TextView>(R.id.lectureName).text = lecturesOfDay[i].name
                lecturesView.findViewById<TextView>(R.id.lectureTime).text = lecturesOfDay[i].time

                itemView.defaultLecturesOfDay.addView(lecturesView)

                if (i != lecturesOfDay.size - 1) {
                    itemView.defaultLecturesOfDay.addView(getSeperatorView())
                }
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
        fun from(parent: ViewGroup,context: Context): DefaultLecturesDayViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_default_lectures, parent, false)

            return DefaultLecturesDayViewHolder(
                    view,
                    context
            )
        }
    }
}
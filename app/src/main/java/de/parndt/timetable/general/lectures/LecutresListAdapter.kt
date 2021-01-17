package de.parndt.timetable.general.lectures

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.parndt.timetable.R
import de.parndt.timetable.lecturesmodels.LecturesDay
import kotlinx.android.synthetic.main.list_item_daily_lectures.view.*


class LecutresListAdapter(val _context: Context) :
        ListAdapter<LecturesDay, LecutresListAdapter.LecturesViewHolder>(LecturesListDiffCallback) {

    private var lecturesList: List<LecturesDay> = mutableListOf()

    inner class LecturesViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {

        fun bind(item: LecturesDay, _context: Context) {
            itemView.lectureDate.text = item.getDate()

            if (itemView.lecturesOfDay.childCount == 0) {

                val lecturesOfDay = item.getLecturesOfDay()

                for (i in lecturesOfDay.indices) {

                    val lecturesView = LayoutInflater.from(_context)
                            .inflate(R.layout.list_item_lecture, null, true)

                    lecturesView.findViewById<TextView>(R.id.lectureName).text = lecturesOfDay[i].name
                    lecturesView.findViewById<TextView>(R.id.lectureTime).text = lecturesOfDay[i].time

                    itemView.lecturesOfDay.addView(lecturesView)

                    if (i != lecturesOfDay.size - 1) {
                        itemView.lecturesOfDay.addView(getSeperatorView())
                    }
                }

            }

        }
    }

    fun getSeperatorView(): View {
        val seperator = View(_context)
        seperator.setBackgroundColor(Color.GRAY)
        seperator.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2)
        return seperator
    }

    fun getItemByPosition(position: Int): LecturesDay {
        return lecturesList[position]
    }

    fun getItemPostionByDate(date:String):Int{
        val index = lecturesList.indexOfFirst { it.getDate() == date }
        return index
    }

    override fun submitList(list: List<LecturesDay>?) {
        super.submitList(list)
        lecturesList = list ?: mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturesViewHolder {
        return LecturesViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_daily_lectures, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LecturesViewHolder, position: Int) {
        holder.bind(getItem(position), _context)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}


object LecturesListDiffCallback : DiffUtil.ItemCallback<LecturesDay>() {
    override fun areItemsTheSame(oldItem: LecturesDay, newItem: LecturesDay): Boolean {
        return oldItem.getDateValue() == newItem.getDateValue()
    }

    override fun areContentsTheSame(oldItem: LecturesDay, newItem: LecturesDay): Boolean {

        var contentSame = true

        for (i in oldItem.getLecturesOfDay().indices) {
            contentSame = oldItem.getLecturesOfDay()[i].name == newItem.getLecturesOfDay()[i].name
            contentSame = oldItem.getLecturesOfDay()[i].date == newItem.getLecturesOfDay()[i].date
            contentSame = oldItem.getLecturesOfDay()[i].time == newItem.getLecturesOfDay()[i].time
        }

        return oldItem.getDateValue() == newItem.getDateValue() && contentSame
    }
}

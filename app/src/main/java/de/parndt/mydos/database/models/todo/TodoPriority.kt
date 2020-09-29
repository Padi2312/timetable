package de.parndt.mydos.database.models.todo

import android.content.Context
import android.graphics.drawable.Drawable
import de.parndt.mydos.R

enum class TodoPriority(val priority: Int) {
    VERY_HIGH(100),
    HIGH(75),
    DEFAULT(50),
    LOW(25),
    VERY_LOW(0);

    companion object {
        fun fromInt(value: Int) = values().first { it.priority == value }
    }
}

fun TodoPriority.getString(context: Context): String {
    return when (this) {
        TodoPriority.VERY_HIGH -> context.getString(R.string.todo_priotiy_very_high)
        TodoPriority.HIGH -> context.getString(R.string.todo_priotiy_high)
        TodoPriority.DEFAULT -> context.getString(R.string.todo_priotiy_default)
        TodoPriority.LOW -> context.getString(R.string.todo_priotiy_low)
        TodoPriority.VERY_LOW -> context.getString(R.string.todo_priotiy_very_low)
    }
}


fun TodoPriority.getIcon(context: Context): Drawable? {
    return when (this) {
        TodoPriority.VERY_HIGH -> context.getDrawable(R.drawable.ic_baseline_keyboard_double_arrow_up_24)
        TodoPriority.HIGH -> context.getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24)
        TodoPriority.DEFAULT -> context.getDrawable(R.drawable.ic_baseline_panorama_fish_eye_24)
        TodoPriority.LOW -> context.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24)
        TodoPriority.VERY_LOW -> context.getDrawable(R.drawable.ic_baseline_keyboard_double_arrow_down_24)
    }
}
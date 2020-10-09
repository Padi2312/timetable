package de.parndt.mydos.utils

import android.content.Context
import android.widget.Button
import androidx.core.content.ContextCompat
import de.parndt.mydos.R

fun setDrawableEndShowMore(button: Button, _context: Context) {
    button.setCompoundDrawablesWithIntrinsicBounds(
        null,
        null,
        ContextCompat.getDrawable(_context, R.drawable.ic_baseline_expand_more_24),
        null
    )
}

fun setDrawableEndShowLess(button: Button, _context: Context) {
    button.setCompoundDrawablesWithIntrinsicBounds(
        null,
        null,
        ContextCompat.getDrawable(_context, R.drawable.ic_baseline_expand_less_24),
        null
    )
}
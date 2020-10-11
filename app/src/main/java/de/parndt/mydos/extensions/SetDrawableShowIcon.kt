package de.parndt.mydos.extensions

import android.content.Context
import android.widget.Button
import androidx.core.content.ContextCompat
import de.parndt.mydos.R

fun Button.setDrawableEndShowMore(_context: Context) {
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        null,
        ContextCompat.getDrawable(_context, R.drawable.ic_baseline_expand_more_24),
        null
    )
}

fun Button.setDrawableEndShowLess(_context: Context) {
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        null,
        ContextCompat.getDrawable(_context, R.drawable.ic_baseline_expand_less_24),
        null
    )
}
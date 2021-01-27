package de.parndt.timetable.update.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import de.parndt.timetable.R
import kotlinx.android.synthetic.main.dialog_update.*

class UpdaterDialogFragment(private val cancel: () -> Unit) : DialogFragment() {

    companion object {
        fun Instance(cancel: () -> Unit): UpdaterDialogFragment {
            return UpdaterDialogFragment(cancel)
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null)
            dialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_update, container, false)
    }

    override fun onResume() {
        super.onResume()
        dialog?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.action != KeyEvent.ACTION_DOWN) {
                true
            } else {
                true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        dialogUpdateCancel.setOnClickListener {
            cancel.invoke()
        }
    }

    fun updateProgress(progress: Long, target: Long) {
        val progressInPercent = ((100.0 / target.toFloat()) * progress.toFloat()).toInt()
        dialogUpdateProgressBar?.progress = progressInPercent
    }
}

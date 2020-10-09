package de.parndt.mydos.views.tabs.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.ui.customcomponent.newtododialog.NewTodoDialogFragment
import de.parndt.mydos.ui.customcomponent.newtododialog.NewTodoDialogResult
import kotlinx.android.synthetic.main.tab_fragment_home.*
import javax.inject.Inject


class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_fragment_home, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

    }

    override fun onResume() {
        super.onResume()
        home_btn_new_todo.setOnClickListener {
            openNewTodoPopUp()
        }
    }

    private fun openNewTodoPopUp() {
        NewTodoDialogFragment.newInstance(object : NewTodoDialogResult {
            override fun addedEntry() {}
        }).show(parentFragmentManager, "dialog_new_todo")

    }
}
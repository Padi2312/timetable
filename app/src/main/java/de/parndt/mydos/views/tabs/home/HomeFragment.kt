package de.parndt.mydos.views.tabs.home

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.utils.dialogs.newtodo.NewTodoDialog
import de.parndt.mydos.utils.dialogs.newtodo.NewTodoDialogResult
import kotlinx.android.synthetic.main.dialog_new_todo.*
import kotlinx.android.synthetic.main.tab_fragment_home.*
import javax.inject.Inject


class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        NewTodoDialog.newInstance(object : NewTodoDialogResult {
            override fun addedEntry() {}
        }).show(parentFragmentManager, "dialog_new_todo")

    }
}
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
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import kotlinx.android.synthetic.main.fragment_tab_home.*
import javax.inject.Inject


class HomeFragment : Fragment() {


    @Inject lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_home, container, false)
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
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(R.layout.popup_new_todo)

        val dialog = builder.create()
        dialog.show()
        dialog.findViewById<Button>(R.id.newtodo_btn_new_todo).setOnClickListener {

            val title = dialog.findViewById<EditText>(R.id.newtodo_input_title).text.toString()
            val content = dialog.findViewById<EditText>(R.id.newtodo_input_content).text.toString()

            homeViewModel.createTodoEntry(title,content)
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.newtodo_btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
    }
}
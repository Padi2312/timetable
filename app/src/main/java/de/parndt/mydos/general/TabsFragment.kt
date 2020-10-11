package de.parndt.mydos.general

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import de.parndt.mydos.R
import de.parndt.mydos.general.tabs.notes.NotesFragment
import de.parndt.mydos.general.tabs.settings.SettingsFragment
import de.parndt.mydos.general.tabs.todos.TodosFragment
import de.parndt.mydos.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_tabs.*

class TabsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).navigateToTab(TodosFragment())
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_todos -> {
                    (activity as MainActivity).navigateToTab(TodosFragment())
                    true
                }
                R.id.tab_notes -> {
                    (activity as MainActivity).navigateToTab(NotesFragment())
                    true
                }
                R.id.tab_settings -> {
                    (activity as MainActivity).navigateToTab(SettingsFragment())
                    true
                }
                else -> {

                    false
                }

            }
        }

    }

}
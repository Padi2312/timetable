package de.parndt.calendar.general.start

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import de.parndt.calendar.R

import de.parndt.calendar.services.backend.BackendService
import kotlinx.android.synthetic.main.fragment_start.*
import java.util.*
import javax.inject.Inject

class StartFragment : Fragment() {

    @Inject lateinit var backendService: BackendService

    @Inject lateinit var _context: Context

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        backendService.status.observe(viewLifecycleOwner, Observer {
            Toast.makeText(_context,it,Toast.LENGTH_SHORT).show()

        })

        btn_test.setOnClickListener {

            backendService.Test()
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onAttach(context: Context) {

        AndroidSupportInjection.inject(this)
        super.onAttach(context)

    }


}
package de.parndt.calendar.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import de.parndt.calendar.R

import de.parndt.calendar.services.network.NetworkService
import kotlinx.android.synthetic.main.fragment_start.*
import javax.inject.Inject

class StartFragment : Fragment() {

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Object>
    @Inject lateinit var networkService: NetworkService

    @Inject lateinit var _context: Context

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        btn_test.setOnClickListener {

            var result = networkService.Test()
            Toast.makeText(_context,result.toString(),Toast.LENGTH_SHORT).show()
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
package de.parndt.calendar.services.network


import android.content.Context
import android.widget.Toast
import okhttp3.OkHttpClient
import retrofit2.*
import javax.inject.Inject

class NetworkService @Inject constructor() {


    @Inject lateinit var _context:Context

    @Inject
    lateinit var retroFit: Retrofit

    fun Test( result: (data:Any) -> Void) {
        val request = retroFit.create(Routes::class.java)
        val call = request.getTest("status")
        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    response.body()?.let { result(it) }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(_context, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
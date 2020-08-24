package de.parndt.calendar.services.backend


import android.os.NetworkOnMainThreadException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import de.parndt.calendar.services.backend.models.Test
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.http.GET
import javax.inject.Inject


interface Routes {
    @GET("/test")
    fun getTest(): Call<Test>
}

class BackendService @Inject constructor() {

    @Inject
    lateinit var retrofit: Retrofit

    val status:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun Test() {

        val api = retrofit.create(Routes::class.java)
        val call = api.getTest()
        GlobalScope.launch {
            call.enqueue(object : Callback<Test> {
                override fun onResponse(
                    call: Call<Test>,
                    response: Response<Test>
                ) {
                    status.postValue(response.body()?.status)
                }

                override fun onFailure(call: Call<Test>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

}
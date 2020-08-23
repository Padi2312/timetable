package de.parndt.calendar.services.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Routes {
    @GET("/test")
    fun getTest(@Query("status")status:String):Call<Any>
}
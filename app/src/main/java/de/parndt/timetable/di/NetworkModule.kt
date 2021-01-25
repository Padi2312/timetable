package de.parndt.timetable.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .followRedirects(false)
        clientBuilder.authenticator(Authenticator { route, response ->
            if (response.request.header("Authorization") != null) {
                return@Authenticator null // Give up, we've already attempted to authenticate.
            }
            println("Authenticating for response: $response")
            System.out.println("Challenges: " + response.challenges())
            val credential = Credentials.basic("admin", "qT7VEQZkzDuN~R@e")
            response.request.newBuilder()
                .header("Authorization", credential)
                .build()
        })
        return clientBuilder.build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(@Named("baseUrl") baseUrl: String, okHttpClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

}
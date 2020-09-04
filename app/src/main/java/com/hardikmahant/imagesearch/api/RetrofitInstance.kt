package com.hardikmahant.imagesearch.api

import com.hardikmahant.imagesearch.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// for getting retrofit instance, using Singleton pattern
class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val gsonConverterFactory = GsonConverterFactory.create()

            val client = OkHttpClient.Builder().addInterceptor(logging).build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(ImageAPI::class.java)
        }
    }
}
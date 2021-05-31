package com.example.vorto.service

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBase {

    fun getApiService(baseUrl:String):ApiService
    {
        val retrofit=Retrofit.Builder()
            .baseUrl("$baseUrl")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(OkHttpClient().newBuilder()
                .connectTimeout(100,TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor())
                .build())
            .build()
        return retrofit.create(ApiService::class.java)

    }
}
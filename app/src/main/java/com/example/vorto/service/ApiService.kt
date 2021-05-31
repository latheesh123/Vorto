package com.example.vorto.service

import com.example.vorto.model.BusinessResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("businesses/search")

    suspend fun getBusiness(
        @Header("Authorization") auth: String,
        @Query("term") query: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<BusinessResponse>
}
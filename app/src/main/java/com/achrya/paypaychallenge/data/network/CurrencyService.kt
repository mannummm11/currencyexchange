package com.achrya.paypaychallenge.data.network

import com.example.example.CurrencyEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("latest.json")
    suspend fun getCurrencyList(@Query("app_id") appId: String): Flow<CurrencyEntity>
}
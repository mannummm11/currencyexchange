package com.achrya.paypaychallenge.data.network

import com.example.example.CurrencyEntity
import retrofit2.http.GET
import retrofit2.http.Query

const val appId = "ab2e8fa5ed84431a89f99fcff5d12b50"

interface CurrencyService {
    @GET("latest.json")
    suspend fun getCurrencyList(@Query("app_id") appId: String): CurrencyEntity
}
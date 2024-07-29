package com.achrya.paypaychallenge.domain.repo

interface CurrencyPreferenceRepo {
    suspend fun saveCurrentTimeStampAndBaseCurr(baseCurr: String, currTime: Long = System.currentTimeMillis())
    suspend fun getTimeStampAndBase(): Pair<Long, String>
}
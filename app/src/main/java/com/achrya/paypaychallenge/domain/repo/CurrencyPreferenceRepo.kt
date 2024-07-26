package com.achrya.paypaychallenge.domain.repo

interface CurrencyPreferenceRepo {
    suspend fun saveCurrentTimeStampAndBaseCurr(baseCurr: String)
    suspend fun getTimeStampAndBase(): Pair<Long, String>
}
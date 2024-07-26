package com.achrya.paypaychallenge.domain.repo

import com.achrya.paypaychallenge.data.db.entity.CurrencyDetail
import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable

interface CurrencyRemoteDataRepo {
    suspend fun getCurrencyList(appId: String): CurrencyDetail
}
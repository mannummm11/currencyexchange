package com.achrya.paypaychallenge.domain.repo

import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable
import com.achrya.paypaychallenge.domain.model.Rate
import kotlinx.coroutines.flow.Flow

interface CurrencyStorageDataRepo {
    suspend fun insertAllCurrencyToDb(currencies: List<CurrencyExchangeTable>)
    suspend fun getAllCurrenciesFromDB(): Flow<List<CurrencyExchangeTable>>
}
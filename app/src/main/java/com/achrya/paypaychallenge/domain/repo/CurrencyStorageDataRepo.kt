package com.achrya.paypaychallenge.domain.repo

import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable
import com.achrya.paypaychallenge.domain.model.Rate

interface CurrencyStorageDataRepo {
    suspend fun insertAllCurrencyToDb(currencies: List<CurrencyExchangeTable>)
    suspend fun getAllCurrenciesFromDB(): List<Rate>
}
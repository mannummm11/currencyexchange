package com.achrya.paypaychallenge.data.repoimpl

import com.achrya.paypaychallenge.data.db.dbHelper.CurrencyDAO
import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.domain.repo.CurrencyStorageDataRepo

class CurrencyStorageRepoImpl(private val currencyDAO: CurrencyDAO) : CurrencyStorageDataRepo {

    override suspend fun insertAllCurrencyToDb(currencies: List<CurrencyExchangeTable>) {
        currencyDAO.insertAllCurrency(currencies)
    }

    override suspend fun getAllCurrenciesFromDB(): List<Rate> {
        return currencyDAO.getAllCurrency().map {
            Rate(it.currency, it.exchangeRate)
        }
    }
}
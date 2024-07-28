package com.achrya.paypaychallenge.data.repoimpl

import com.achrya.paypaychallenge.data.db.dbHelper.CurrencyDAO
import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.domain.repo.CurrencyStorageDataRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class CurrencyStorageRepoImpl(private val currencyDAO: CurrencyDAO) : CurrencyStorageDataRepo {

    override suspend fun insertAllCurrencyToDb(currencies: List<CurrencyExchangeTable>) {
        currencyDAO.insertAllCurrency(currencies)
    }

    override suspend fun getAllCurrenciesFromDB(): Flow<List<Rate>> {
        return flow {
            currencyDAO.getAllCurrency().collect { rates->
                emit(rates.map {
                    Rate(it.currency, it.exchangeRate)
                })
            }
        }
    }
}
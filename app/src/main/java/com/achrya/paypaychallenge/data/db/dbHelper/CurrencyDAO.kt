package com.achrya.paypaychallenge.data.db.dbHelper

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable

@Dao
interface CurrencyDAO {
    @Upsert
    suspend fun insertAllCurrency(currencies: List<CurrencyExchangeTable>)

    @Query("SELECT * FROM currency_exchange_rate")
    suspend fun getAllCurrency(): List<CurrencyExchangeTable>
}
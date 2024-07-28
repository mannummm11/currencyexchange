package com.achrya.paypaychallenge.data.db.dbHelper

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.achrya.paypaychallenge.data.db.DB_FETCH_FROM_EXCHANGERATE
import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable
import kotlinx.coroutines.flow.Flow


@Dao
interface CurrencyDAO {
    @Upsert
    suspend fun insertAllCurrency(currencies: List<CurrencyExchangeTable>)

    @Query(DB_FETCH_FROM_EXCHANGERATE)
    fun getAllCurrency(): Flow<List<CurrencyExchangeTable>>
}
package com.achrya.paypaychallenge.data.db.dbHelper

import androidx.room.Database
import androidx.room.RoomDatabase
import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable

@Database(entities = [CurrencyExchangeTable::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDAO
}
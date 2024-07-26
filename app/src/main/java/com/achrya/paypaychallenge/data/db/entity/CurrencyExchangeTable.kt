package com.achrya.paypaychallenge.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_exchange_rate")
data class CurrencyExchangeTable(
    @PrimaryKey @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "exchangeRate") val exchangeRate: Float,
)

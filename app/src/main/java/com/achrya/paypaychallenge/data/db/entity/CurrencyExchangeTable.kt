package com.achrya.paypaychallenge.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.achrya.paypaychallenge.data.db.CURRENCY_TABLE_NAME

@Entity(tableName = CURRENCY_TABLE_NAME)
data class CurrencyExchangeTable(
    @PrimaryKey @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "exchangeRate") val exchangeRate: Float,
)

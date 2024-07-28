package com.achrya.paypaychallenge.data.mapper

import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable

data class CurrencyDetail(
    val base: String,
    val allCurrencyExchange: List<CurrencyExchangeTable>
)

package com.achrya.paypaychallenge.data.db.entity

data class CurrencyDetail(
    val base: String,
    val allCurrencyExchange: List<CurrencyExchangeTable>
)

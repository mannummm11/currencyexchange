package com.achrya.paypaychallenge.data.mapper

import com.achrya.paypaychallenge.data.db.entity.CurrencyDetail
import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable
import com.achrya.paypaychallenge.domain.model.Currency
import com.achrya.paypaychallenge.domain.model.Rate
import com.example.example.CurrencyEntity

fun CurrencyEntity.toDomainCurrency(): Currency {
    return Currency(
        this.timestamp ?: System.currentTimeMillis(),
        this.base,
        this.rates.map { Rate(it.currency, it.exchangeRate) })
}

fun CurrencyEntity.toCurrencyExchangeTable() =
    CurrencyDetail(this.base ?: "",
        this.rates.map {
            CurrencyExchangeTable(currency = it.currency, exchangeRate = it.exchangeRate)
        })
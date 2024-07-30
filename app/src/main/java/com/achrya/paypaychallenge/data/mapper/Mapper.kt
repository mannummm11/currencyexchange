package com.achrya.paypaychallenge.data.mapper

import com.achrya.paypaychallenge.data.db.entity.CurrencyExchangeTable
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.data.entity.CurrencyEntity

fun CurrencyEntity.toCurrencyExchangeTable() =
    CurrencyDetail(this.base ?: "",
        this.rates.map {
            CurrencyExchangeTable(currency = it.currency, exchangeRate = it.exchangeRate)
        })

fun CurrencyExchangeTable.toDomainRate(): Rate {
    return Rate(this.currency, this.exchangeRate)
}
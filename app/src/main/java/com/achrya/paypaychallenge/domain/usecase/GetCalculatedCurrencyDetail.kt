package com.achrya.paypaychallenge.domain.usecase

import com.achrya.paypaychallenge.domain.model.Currency
import com.achrya.paypaychallenge.domain.model.Rate

class GetCalculatedCurrencyDetail {
    suspend operator fun invoke(selectedCurrency: String, selectedExchange: Float, moneyUnit: Int, rates: List<Rate> ): Currency {
        rates.map {
            it.exchangeRate * (moneyUnit/selectedExchange)
        }
        return Currency(0, selectedCurrency, rates)
    }
}
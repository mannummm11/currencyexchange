package com.achrya.paypaychallenge.domain.usecase

import com.achrya.paypaychallenge.domain.model.Currency
import com.achrya.paypaychallenge.domain.model.Rate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.DecimalFormat

class GetCalculatedCurrencyDetail {
    suspend operator fun invoke(selectedCurrency: Rate, moneyUnit: Float, rates: List<Rate> ): Flow<Currency> {
        return flow {
            val updatedRates = rates.map { rate->
                rate.copy(
                    exchangeRate = rate.exchangeRate * (moneyUnit/selectedCurrency.exchangeRate)
                )
            }
             emit(Currency(0, selectedCurrency.currency, updatedRates))
        }.flowOn(Dispatchers.IO)
    }
}
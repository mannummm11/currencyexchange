package com.achrya.paypaychallenge.domain.usecase

import com.achrya.paypaychallenge.domain.model.Currency
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCalculatedCurrencyDetail {
    suspend operator fun invoke(
        selectedCurrency: Rate,
        moneyUnit: Float,
        rates: List<Rate>
    ): Flow<NetworkResult<Currency>> {
        return flow {
            if (moneyUnit <= 0 || selectedCurrency.exchangeRate <= 0) {
                throw IllegalArgumentException("Money unit should be greater than zero")
            }
            if (rates.find { it.exchangeRate <= 0 } != null) {
                throw IllegalArgumentException("Exchange rate should be greater than zero")
            }
            emit(NetworkResult.Loading(true))
            val updatedRates = rates.map { rate ->
                rate.copy(
                    exchangeRate = rate.exchangeRate * (moneyUnit / selectedCurrency.exchangeRate)
                )
            }
            emit(NetworkResult.Loading(false))
            emit(NetworkResult.Success(Currency(0, selectedCurrency.currency, updatedRates)))
        }.catch { ex ->
            emit(NetworkResult.Loading(false))
            emit(NetworkResult.Error<Currency>(ex.message ?: "Something went wrong"))
        }.flowOn(Dispatchers.IO)

    }
}
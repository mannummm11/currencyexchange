package com.achrya.paypaychallenge.domain.usecase

import com.achrya.paypaychallenge.BuildConfig
import com.achrya.paypaychallenge.data.mapper.toCurrencyExchangeTable
import com.achrya.paypaychallenge.data.mapper.toDomainRate
import com.achrya.paypaychallenge.domain.model.Currency
import com.achrya.paypaychallenge.domain.repo.CurrencyPreferenceRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyRemoteDataRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyStorageDataRepo
import com.achrya.paypaychallenge.utils.NetworkResult
import com.achrya.paypaychallenge.utils.TIME_LIMIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class GetLatestCurrencyDetail(
    private val remoteData: CurrencyRemoteDataRepo,
    private val localDbData: CurrencyStorageDataRepo,
    private val sharedPref: CurrencyPreferenceRepo
) {
    suspend operator fun invoke(): Flow<NetworkResult<Currency>> {
        return flow {
            emit(NetworkResult.Loading(true))
            if (isTimeExceed(sharedPref.getTimeStampAndBase().first)) {
                remoteData.getCurrencyList(BuildConfig.APP_ID).collect { currEntity ->
                    currEntity.toCurrencyExchangeTable().let { currDetail ->
                        localDbData.insertAllCurrencyToDb(currDetail.allCurrencyExchange)
                        sharedPref.saveCurrentTimeStampAndBaseCurr(currDetail.base)
                    }
                }
            }
            localDbData.getAllCurrenciesFromDB().collect { rates ->
                emit(NetworkResult.Loading(false))
                emit(
                    NetworkResult.Success(
                        Currency(
                            sharedPref.getTimeStampAndBase().first,
                            sharedPref.getTimeStampAndBase().second,
                            rates.map { it.toDomainRate() }
                        )
                    )
                )
            }
        }.catch { ex ->
            emit(NetworkResult.Loading(false))
            emit(NetworkResult.Error(ex.message ?: "Something went wrong")) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun updateLatestDetailToDB() {
        if (isTimeExceed(sharedPref.getTimeStampAndBase().first)) {
            remoteData.getCurrencyList(BuildConfig.APP_ID).collect { currEntity ->
                currEntity.toCurrencyExchangeTable().let { currDetail ->
                    localDbData.insertAllCurrencyToDb(currDetail.allCurrencyExchange)
                    sharedPref.saveCurrentTimeStampAndBaseCurr(currDetail.base)
                }
            }
        }
    }

    private fun isTimeExceed(lastTime: Long): Boolean {
        val currTime = System.currentTimeMillis()
        return (currTime - lastTime) > TIME_LIMIT
    }
}
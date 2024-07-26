package com.achrya.paypaychallenge.domain.usecase

import com.achrya.paypaychallenge.domain.model.Currency
import com.achrya.paypaychallenge.domain.repo.CurrencyPreferenceRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyRemoteDataRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyStorageDataRepo

const val appId = "ab2e8fa5ed84431a89f99fcff5d12b50"

class GetLatestCurrencyDetail(
    private val remoteData: CurrencyRemoteDataRepo,
    private val localDbData: CurrencyStorageDataRepo,
    private val sharedPref: CurrencyPreferenceRepo
) {

    suspend operator fun invoke(): Currency {
        if (isTimeExceed(sharedPref.getTimeStampAndBase().first)) {
            val currDetail = remoteData.getCurrencyList(appId)
            localDbData.insertAllCurrencyToDb(currDetail.allCurrencyExchange)
            sharedPref.saveCurrentTimeStampAndBaseCurr(currDetail.base)
        }
        return Currency(
            sharedPref.getTimeStampAndBase().first,
            sharedPref.getTimeStampAndBase().second,
            localDbData.getAllCurrenciesFromDB()
        )
    }

    private fun isTimeExceed(lastTime: Long): Boolean {
        val currTime = System.currentTimeMillis()
        return (currTime - lastTime) >= 30 * 60 * 1000
    }
}
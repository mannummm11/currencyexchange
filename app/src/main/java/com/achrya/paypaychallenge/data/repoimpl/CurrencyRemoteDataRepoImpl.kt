package com.achrya.paypaychallenge.data.repoimpl

import com.achrya.paypaychallenge.data.mapper.toCurrencyExchangeTable
import com.achrya.paypaychallenge.data.network.CurrencyService
import com.achrya.paypaychallenge.domain.repo.CurrencyRemoteDataRepo

class CurrencyRemoteDataRepoImpl(private val currencyService: CurrencyService) : CurrencyRemoteDataRepo {
    override suspend fun getCurrencyList(appId: String) =
        currencyService.getCurrencyList(appId).toCurrencyExchangeTable()
}
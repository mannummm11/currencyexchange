package com.achrya.paypaychallenge.domain.repo

import com.achrya.paypaychallenge.data.mapper.CurrencyDetail
import kotlinx.coroutines.flow.Flow

interface CurrencyRemoteDataRepo {
    suspend fun getCurrencyList(appId: String): Flow<CurrencyDetail>
}
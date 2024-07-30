package com.achrya.paypaychallenge.domain.repo

import com.achrya.paypaychallenge.data.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface CurrencyRemoteDataRepo {
    suspend fun getCurrencyList(appId: String): Flow<CurrencyEntity>
}
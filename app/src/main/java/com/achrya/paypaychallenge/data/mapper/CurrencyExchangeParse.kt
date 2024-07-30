package com.achrya.paypaychallenge.data.mapper

import com.achrya.paypaychallenge.data.entity.RateEntity
import com.achrya.paypaychallenge.data.entity.CurrencyEntity
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.reflect.Type

const val BASE = "base"
const val TIMESTAMP = "timestamp"
const val DISCLAIMER = "disclaimer"
const val LICENSE = "license"
const val RATES = "rates"

class CurrencyExchangeParse : JsonDeserializer<Flow<CurrencyEntity>> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Flow<CurrencyEntity> {
        return flow {
            if (json == null || context == null) {
                throw Exception("Error")
            }
            val obj = json.asJsonObject
            val base = context.deserialize<String?>(obj.get(BASE), String::class.java)
            val timestamp = context.deserialize<Long?>(obj.get(TIMESTAMP), Long::class.java)
            val disclaimer = context.deserialize<String>(obj.get(DISCLAIMER), String::class.java)
            val license  = context.deserialize<String>(obj.get(LICENSE), String::class.java)

            val ratesSet = obj.get(RATES).asJsonObject.entrySet()
            val ratesList = ratesSet.map {
                val currency = it.key
                val exchangeRate = it.value.asFloat
                RateEntity(currency, exchangeRate)
            }

            emit(CurrencyEntity(disclaimer, license, timestamp, base, ratesList))
        }.flowOn(Dispatchers.IO)
    }
}
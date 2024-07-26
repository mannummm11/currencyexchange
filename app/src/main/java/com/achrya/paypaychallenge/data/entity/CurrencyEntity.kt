package com.example.example

import com.achrya.paypaychallenge.data.entity.RateEntity


data class CurrencyEntity(
    var disclaimer: String? = null,
    var license: String? = null,
    var timestamp: Long? = null,
    var base: String? = null,
    var rates: List<RateEntity> = emptyList()
)
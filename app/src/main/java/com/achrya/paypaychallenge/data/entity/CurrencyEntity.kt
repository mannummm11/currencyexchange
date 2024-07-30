package com.achrya.paypaychallenge.data.entity

data class CurrencyEntity(
    var disclaimer: String? = null,
    var license: String? = null,
    var timestamp: Long? = null,
    var base: String? = null,
    var rates: List<RateEntity> = emptyList()
)
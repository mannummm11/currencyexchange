package com.achrya.paypaychallenge.domain.model

data class Currency(
    val timestamp: Long,
    val base: String? = null,
    val rates: List<Rate>? = null
)

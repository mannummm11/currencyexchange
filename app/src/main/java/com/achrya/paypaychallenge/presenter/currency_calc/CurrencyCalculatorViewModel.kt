package com.achrya.paypaychallenge.presenter.currency_calc

import androidx.lifecycle.ViewModel
import com.achrya.paypaychallenge.domain.usecase.GetLatestCurrencyDetail

class CurrencyCalculatorViewModel(
    private val getLatestCurrencyDetail: GetLatestCurrencyDetail
) : ViewModel() {
    suspend fun getRefreshData() = getLatestCurrencyDetail()
}
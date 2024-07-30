package com.achrya.paypaychallenge.presenter.currency_calc

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.domain.usecase.GetCalculatedCurrencyDetail
import com.achrya.paypaychallenge.domain.usecase.GetLatestCurrencyDetail
import com.achrya.paypaychallenge.utils.NetworkResult

data class CurrencyUiState(
    var amount: String = "1",
    var baseCurr: String = "Select Currency",
    var currDetail: List<Rate> = emptyList(),
    var showProgressBar: Boolean = false,
    var showError: Boolean = false
)

class CurrencyCalculatorViewModel(
    private val getLatestCurrencyDetail: GetLatestCurrencyDetail,
    private val getCalculatedCurrencyDetail: GetCalculatedCurrencyDetail
) : ViewModel() {
    private var _currencyUiState = mutableStateOf(CurrencyUiState())
    val currencyUiState: CurrencyUiState get() = _currencyUiState.value

    suspend fun getRefreshData() {
        getLatestCurrencyDetail().collect { networkRes ->
            when(networkRes) {
                is NetworkResult.Loading -> {
                    _currencyUiState.value = _currencyUiState.value.copy(
                        showProgressBar = networkRes.isLoading
                    )
                }
                is NetworkResult.Success -> {
                    _currencyUiState.value = _currencyUiState.value.copy(
                        amount = "1",
                        baseCurr = networkRes._data?.base ?: "Select Currency",
                        currDetail = networkRes._data?.rates ?: emptyList(),
                        showProgressBar = false, showError = false
                    )
                }
                is NetworkResult.Error -> {
                    _currencyUiState.value = _currencyUiState.value.copy(
                        showError = true
                    )
                }
            }
        }
    }

     suspend fun getCalculatedData(selectedCurr: Rate, rates: List<Rate>, amount: Float) {
        getCalculatedCurrencyDetail(selectedCurr, amount, rates).collect { networkResult->
            when(networkResult) {
                is NetworkResult.Loading -> {
                    _currencyUiState.value = _currencyUiState.value.copy(
                        showProgressBar = networkResult.isLoading
                    )
                }
                is NetworkResult.Success -> {
                    _currencyUiState.value = _currencyUiState.value.copy(
                        amount = amount.toString(),
                        baseCurr = networkResult.data?.base ?: "",
                        currDetail = networkResult.data?.rates ?: emptyList()
                    )
                    getLatestCurrencyDetail.updateLatestDetailToDB()
                }
                is NetworkResult.Error -> {
                    _currencyUiState.value = _currencyUiState.value.copy(
                        showError = true
                    )
                }
            }
        }
    }

    fun updateAmountField(text: String) {
        _currencyUiState.value = _currencyUiState.value.copy(amount = text.ifEmpty { "0" })
    }

}
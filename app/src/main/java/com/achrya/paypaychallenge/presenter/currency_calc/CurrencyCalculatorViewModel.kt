package com.achrya.paypaychallenge.presenter.currency_calc

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.achrya.paypaychallenge.domain.model.Rate
import com.achrya.paypaychallenge.domain.usecase.GetCalculatedCurrencyDetail
import com.achrya.paypaychallenge.domain.usecase.GetLatestCurrencyDetail
import com.achrya.paypaychallenge.utils.NetworkResult

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
        getCalculatedCurrencyDetail(selectedCurr, amount, rates).collect { curr->
            _currencyUiState.value = _currencyUiState.value.copy(
                amount = amount.toString(),
                baseCurr = curr.base ?: "",
                currDetail = curr.rates ?: emptyList()
            )
        }
    }



    fun updateAmountField(text: String) {
        _currencyUiState.value = _currencyUiState.value.copy(amount = text.ifEmpty { "0" })
    }

}

data class CurrencyUiState(
    val amount: String = "1",
    val baseCurr: String = "Select Currency",
    val currDetail: List<Rate> = emptyList(),
    val showProgressBar: Boolean = false,
    val showError: Boolean = false
)
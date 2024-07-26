package com.achrya.paypaychallenge.presenter

import androidx.lifecycle.viewmodel.compose.viewModel
import com.achrya.paypaychallenge.presenter.currency_calc.CurrencyCalculatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { CurrencyCalculatorViewModel(get()) }
}
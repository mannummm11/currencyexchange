package com.achrya.paypaychallenge.domain.di

import com.achrya.paypaychallenge.domain.repo.CurrencyPreferenceRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyRemoteDataRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyStorageDataRepo
import com.achrya.paypaychallenge.domain.usecase.GetCalculatedCurrencyDetail
import com.achrya.paypaychallenge.domain.usecase.GetLatestCurrencyDetail
import org.koin.dsl.module

fun provideGetLatestCurrencyDetail(
    remoteData: CurrencyRemoteDataRepo,
    localDbData: CurrencyStorageDataRepo,
    sharedPref: CurrencyPreferenceRepo
) = GetLatestCurrencyDetail(remoteData, localDbData, sharedPref)

val domainUseCase = module {
    factory { provideGetLatestCurrencyDetail(get(), get(), get()) }
    factory { GetCalculatedCurrencyDetail() }
}

package com.achrya.paypaychallenge.domain.di

import android.content.SharedPreferences
import com.achrya.paypaychallenge.data.db.dbHelper.CurrencyDAO
import com.achrya.paypaychallenge.data.network.CurrencyService
import com.achrya.paypaychallenge.data.repoimpl.CurrencyRemoteDataRepoImpl
import com.achrya.paypaychallenge.data.repoimpl.CurrencyStorageRepoImpl
import com.achrya.paypaychallenge.data.repoimpl.CurrencySharedPreferenceRepoImpl
import com.achrya.paypaychallenge.domain.repo.CurrencyRemoteDataRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyStorageDataRepo
import com.achrya.paypaychallenge.domain.repo.CurrencyPreferenceRepo
import org.koin.dsl.module

fun provideCurrencyRemoteDataRepo(currencyService: CurrencyService): CurrencyRemoteDataRepo {
    return CurrencyRemoteDataRepoImpl(currencyService)
}

fun provideCurrencyStorageDataRepo(currencyDAO: CurrencyDAO): CurrencyStorageDataRepo {
    return CurrencyStorageRepoImpl(currencyDAO)
}

fun provideSharedPreferenceRepo(sharedPref: SharedPreferences): CurrencyPreferenceRepo {
    return CurrencySharedPreferenceRepoImpl(sharedPref)
}

val repoModule = module {
    factory { provideCurrencyRemoteDataRepo(get()) }
    factory { provideCurrencyStorageDataRepo(get()) }
    factory { provideSharedPreferenceRepo(get()) }
}
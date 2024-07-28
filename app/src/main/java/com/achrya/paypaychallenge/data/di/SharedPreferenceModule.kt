package com.achrya.paypaychallenge.data.di

import android.content.Context
import android.content.SharedPreferences
import com.achrya.paypaychallenge.data.db.PRFS_FILE_NAME
import com.achrya.paypaychallenge.data.repoimpl.CurrencySharedPreferenceRepoImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun provideSharedPreference(context: Context): SharedPreferences =
    context.getSharedPreferences(PRFS_FILE_NAME, Context.MODE_PRIVATE)

fun provideLocalSharePrefe(sharedPreference: SharedPreferences): CurrencySharedPreferenceRepoImpl {
    return CurrencySharedPreferenceRepoImpl(sharedPreference)
}
val sharedPreferences = module {
    single { provideSharedPreference(androidContext()) }
    single { provideLocalSharePrefe(get()) }
}
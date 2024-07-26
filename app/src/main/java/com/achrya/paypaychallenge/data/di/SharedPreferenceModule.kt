package com.achrya.paypaychallenge.data.di

import android.content.Context
import android.content.SharedPreferences
import com.achrya.paypaychallenge.data.repoimpl.CurrencySharedPreferenceRepoImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun provideSharedPreference(context: Context): SharedPreferences =
    context.getSharedPreferences("curr_private", Context.MODE_PRIVATE)

fun provideLocalSharePrefe(sharedPreference: SharedPreferences): CurrencySharedPreferenceRepoImpl {
    return CurrencySharedPreferenceRepoImpl(sharedPreference)
}
val sharedPreferences = module {
    single { provideSharedPreference(androidContext()) }
    single { provideLocalSharePrefe(get()) }
}
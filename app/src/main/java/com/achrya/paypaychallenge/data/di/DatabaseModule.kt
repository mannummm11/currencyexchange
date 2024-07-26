package com.achrya.paypaychallenge.data.di

import androidx.room.Room
import com.achrya.paypaychallenge.data.db.dbHelper.CurrencyDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CurrencyDatabase::class.java, "currency-database"
        ).build()
    }

    single { get<CurrencyDatabase>().getCurrencyDao() }
}
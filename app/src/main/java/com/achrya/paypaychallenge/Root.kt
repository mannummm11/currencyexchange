package com.achrya.paypaychallenge

import android.app.Application
import com.achrya.paypaychallenge.data.di.dbModule
import com.achrya.paypaychallenge.data.di.networkModule
import com.achrya.paypaychallenge.data.di.sharedPreferences
import com.achrya.paypaychallenge.domain.di.domainUseCase
import com.achrya.paypaychallenge.domain.di.repoModule
import com.achrya.paypaychallenge.presenter.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Root : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Root)
            modules(
                listOf(
                    networkModule,
                    dbModule,
                    sharedPreferences,
                    repoModule,
                    domainUseCase,
                    appModule
                )
            )
        }
    }
}
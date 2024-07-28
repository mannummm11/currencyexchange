package com.achrya.paypaychallenge

import android.app.Application
import androidx.compose.runtime.Composable
import com.achrya.paypaychallenge.data.db.dbHelper.CurrencyDatabase
import com.achrya.paypaychallenge.data.di.dbModule
import com.achrya.paypaychallenge.data.di.networkModule
import com.achrya.paypaychallenge.data.di.sharedPreferences
import com.achrya.paypaychallenge.domain.di.domainUseCase
import com.achrya.paypaychallenge.domain.di.repoModule
import com.achrya.paypaychallenge.presenter.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.getKoin
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class Root : Application(), KoinComponent {
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

    override fun onTerminate() {
        super.onTerminate()
        val db = getKoin().get<CurrencyDatabase>()
        db.close()
        stopKoin()
    }
}
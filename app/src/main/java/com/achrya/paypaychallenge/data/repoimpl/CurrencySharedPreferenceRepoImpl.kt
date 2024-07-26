package com.achrya.paypaychallenge.data.repoimpl

import android.content.SharedPreferences
import com.achrya.paypaychallenge.domain.repo.CurrencyPreferenceRepo

class CurrencySharedPreferenceRepoImpl(private val sharedPref: SharedPreferences) : CurrencyPreferenceRepo {
    override suspend fun saveCurrentTimeStampAndBaseCurr(baseCurr: String) {
        sharedPref.edit().run {
            putLong("curr_timestamp", System.currentTimeMillis())
            putString("base_currency", baseCurr)
            apply()
        }
    }

    override suspend fun getTimeStampAndBase(): Pair<Long, String> =
        Pair(
            first = sharedPref.getLong("curr_timestamp", 0L),
            second = sharedPref.getString("base_currency", "") ?: ""
        )

}
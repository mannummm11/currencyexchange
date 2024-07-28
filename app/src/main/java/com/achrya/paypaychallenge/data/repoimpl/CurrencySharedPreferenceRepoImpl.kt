package com.achrya.paypaychallenge.data.repoimpl

import android.content.SharedPreferences
import com.achrya.paypaychallenge.domain.repo.CurrencyPreferenceRepo

const val CURR_TIMESTAMP = "curr_timestamp"
const val BASE_CURR = "base_currency"
class CurrencySharedPreferenceRepoImpl(private val sharedPref: SharedPreferences) : CurrencyPreferenceRepo {
    override suspend fun saveCurrentTimeStampAndBaseCurr(baseCurr: String) {
        sharedPref.edit().run {
            putLong(CURR_TIMESTAMP, System.currentTimeMillis())
            putString(BASE_CURR, baseCurr)
            apply()
        }
    }

    override suspend fun getTimeStampAndBase(): Pair<Long, String> =
        Pair(
            first = sharedPref.getLong(CURR_TIMESTAMP, 0L),
            second = sharedPref.getString(BASE_CURR, "") ?: ""
        )

}
package com.achrya.paypaychallenge.data.di

import com.achrya.paypaychallenge.data.mapper.CurrencyExchangeParse
import com.achrya.paypaychallenge.data.network.CurrencyService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


fun provideOkhttpClient() = OkHttpClient.Builder()
    .readTimeout(30, TimeUnit.SECONDS)
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

fun provideCurrencyGsonConvertFactory() = GsonConverterFactory.create(
    GsonBuilder()
        .registerTypeAdapter(Flow::class.java, CurrencyExchangeParse())
        .create()
)

fun provideRetrofitWithCurrencyGsonAdapter(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
) = Retrofit.Builder()
    .addConverterFactory(gsonConverterFactory)
    .client(okHttpClient)
    .baseUrl(com.achrya.paypaychallenge.BuildConfig.BASE_API_URL)
    .build()

fun provideCurrencyApiService(retrofit: Retrofit) = retrofit.create(CurrencyService::class.java)

val networkModule = module {
    single { provideOkhttpClient() }
    single { provideCurrencyGsonConvertFactory() }
    single { provideRetrofitWithCurrencyGsonAdapter(get(), get()) }
    single { provideCurrencyApiService(get()) }

}
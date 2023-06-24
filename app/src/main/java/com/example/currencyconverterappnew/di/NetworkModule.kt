package com.example.currencyconverterappnew.di

import com.example.currencyconverterappnew.data.api.CurrencyApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<CurrencyApi> {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.exchangerate.host")
            .build()

        retrofit.create(CurrencyApi::class.java)
    }
}
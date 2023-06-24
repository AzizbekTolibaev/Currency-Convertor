package com.example.currencyconverterappnew.data.api

import com.example.currencyconverterappnew.data.remote.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/convert")
    suspend fun getRates(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String,
        @Query("date") date: String
    ): Response<CurrencyResponse>
}
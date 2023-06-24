package com.example.currencyconverterappnew.data.remote

data class CurrencyResponse(
    val success: Boolean,
    val query: CurrencyQuery,
    val info: CurrencyInfo,
    val historical: Boolean,
    val date: String,
    val result: Double?
)

data class CurrencyQuery(
    val from: String,
    val to: String,
    val amount: Double
)

data class CurrencyInfo(
    val rate: Double?
)

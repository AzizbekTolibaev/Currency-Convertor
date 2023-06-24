package com.example.currencyconverterappnew.domain.repository.`interface`

import com.example.currencyconverterappnew.utils.ResultData
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getRates(from: String, to: String, amount: String, date: String): Flow<ResultData<Double?>>
}
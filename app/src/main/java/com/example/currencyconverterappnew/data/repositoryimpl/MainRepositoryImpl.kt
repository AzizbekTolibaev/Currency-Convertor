package com.example.currencyconverterappnew.data.repositoryimpl

import com.example.currencyconverterappnew.data.api.CurrencyApi
import com.example.currencyconverterappnew.domain.repository.`interface`.MainRepository
import com.example.currencyconverterappnew.utils.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepositoryImpl(private val api: CurrencyApi): MainRepository {

    override suspend fun getRates(
        from: String,
        to: String,
        amount: String,
        date: String
    ) = flow {
        emit(ResultData.Loading)

        val response = api.getRates(from, to, amount, date)
        val result = response.body()!!.result
        if (response.isSuccessful) {
            emit(ResultData.Success(result))
        } else {
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)
}
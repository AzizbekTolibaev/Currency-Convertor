package com.example.currencyconverterappnew.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverterappnew.data.repositoryimpl.MainRepositoryImpl
import com.example.currencyconverterappnew.utils.ResultData
import com.example.currencyconverterappnew.utils.realDate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    private val repository: MainRepositoryImpl,
) : ViewModel() {

    private val _conversion = MutableLiveData<Double?>()
    val conversion get() = _conversion

    private val _loading = MutableLiveData<Boolean>()
    val loading get() = _loading

    private val _message = MutableLiveData<String>()
    val message get() = _message

    private val _error = MutableLiveData<Throwable>()
    val error get() = _error

    private val _oneCurrency = MutableLiveData<String?>()
    val oneCurrency: LiveData<String?> get() = _oneCurrency

    private val _currencyRise = MutableLiveData<Boolean>()
    val currencyRise: LiveData<Boolean> get() = _currencyRise

    private val realDate = realDate(0)
    private val yesterdayDate = realDate(-1)
    private var currency = 0

    suspend fun convert(from: String, to: String, amount: String) {

        repository.getRates(from, to, amount, realDate).onEach {
            when (it) {
                is ResultData.Loading -> {
                    _loading.value = true
                }
                is ResultData.Success -> {
                    _conversion.value = it.data
                    _loading.value = false
                }
                is ResultData.Message -> {
                    _message.value = it.message
                    _loading.value = false
                }
                is ResultData.Error -> {
                    _error.value = it.error
                    _loading.value = false
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun currencyRise(from: String, to: String) {

        repository.getRates(from, to, "1", realDate).onEach { it2 ->
            when (it2) {
                is ResultData.Loading -> {}
                is ResultData.Success -> {
                    val formatResult = String.format("%.2f", it2.data).replace(",", ".")
                    _oneCurrency.value = "1 $from = $formatResult $to"
                    currency = it2.data!!.toInt()
                }
                is ResultData.Message -> { }
                is ResultData.Error -> { }
            }
        }.launchIn(viewModelScope)

        repository.getRates(from, to, "1", yesterdayDate).onEach { it3 ->
            when (it3) {
                is ResultData.Loading -> {}
                is ResultData.Success -> {
                    _currencyRise.value = currency > it3.data!!.toInt()
                }
                is ResultData.Message -> {}
                is ResultData.Error -> {}
            }
        }.launchIn(viewModelScope)
    }
}

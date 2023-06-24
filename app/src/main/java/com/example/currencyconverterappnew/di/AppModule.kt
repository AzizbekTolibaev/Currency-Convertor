package com.example.currencyconverterappnew.di

import com.example.currencyconverterappnew.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel(repository = get())
    }
}
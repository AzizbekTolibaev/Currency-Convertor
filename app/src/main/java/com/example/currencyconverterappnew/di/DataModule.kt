package com.example.currencyconverterappnew.di

import com.example.currencyconverterappnew.data.repositoryimpl.MainRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<MainRepositoryImpl> {
        MainRepositoryImpl(api = get())
    }
}
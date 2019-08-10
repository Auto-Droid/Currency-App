package com.sourabhkarkal.currency.di

import android.arch.lifecycle.ViewModel
import com.sourabhkarkal.currency.viewmodel.ConverterViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class  ConverterViewModelModule {
    @Singleton
    @Provides
    internal fun providesConverterViewModel(): ViewModel {
        return ConverterViewModel()
    }
}
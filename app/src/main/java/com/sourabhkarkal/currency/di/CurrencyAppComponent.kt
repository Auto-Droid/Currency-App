package com.sourabhkarkal.currency.di

import android.app.Application
import android.content.Context
import com.sourabhkarkal.currency.CurrencyApp
import com.sourabhkarkal.currency.viewmodel.ConverterViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    MainActivityModule::class,
    ConverterFragmentModule::class,
    ServiceModule::class,
    RepositoryModule::class])
interface CurrencyAppComponent {

    fun inject(application: CurrencyApp)
    fun inject(converterViewModel: ConverterViewModel)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun context(context: Context): Builder
        fun build(): CurrencyAppComponent
    }
}
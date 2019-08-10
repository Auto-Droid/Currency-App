package com.sourabhkarkal.currency.di

import com.sourabhkarkal.currency.view.fragment.ConverterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ConverterFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun converterFragment(): ConverterFragment
}
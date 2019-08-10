package com.sourabhkarkal.currency.di

import com.sourabhkarkal.currency.view.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity
}

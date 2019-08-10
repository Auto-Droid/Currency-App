package com.sourabhkarkal.currency

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import com.sourabhkarkal.currency.di.CurrencyAppComponent
import com.sourabhkarkal.currency.di.DaggerCurrencyAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class CurrencyApp : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityDispatcher: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentDispatcher: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatcher

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatcher

    companion object {
        lateinit var appComponent: CurrencyAppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerCurrencyAppComponent
            .builder()
            .application(this)
            .context(this)
            .build()

        appComponent.inject(this)

    }


}
package com.sourabhkarkal.currency.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.sourabhkarkal.currency.BuildConfig
import com.sourabhkarkal.currency.CurrencyApp
import com.sourabhkarkal.currency.model.Rate
import com.sourabhkarkal.currency.interactors.subscriber.RateSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConverterViewModel : ViewModel {

    companion object {
        const val DEFAULT_SYMBOL = "EUR"
    }
    @Inject lateinit  var ratesSubscriber: RateSubscriber
    private var currencyList = MutableLiveData<ArrayList<Rate>>()
    private var amountData = MutableLiveData<Double>()
    private var dialogState = MutableLiveData<Int>()
    private var errorData = MutableLiveData<String>()

    private var currentBase: String = ""
    private var isLoaded = false
    private var disposable : Disposable? = null;

    constructor() : super() {
        CurrencyApp.appComponent.inject(this)
    }

    fun getCurrencyLiveData(): LiveData<ArrayList<Rate>> {
        return currencyList
    }


    fun getDialogState(): LiveData<Int> {
        return dialogState
    }

    fun getAmountLiveData(): LiveData<Double> {
        return amountData
    }

    fun getErrorData(): LiveData<String> {
        return errorData
    }

    @SuppressLint("CheckResult")
    fun checkRates(base: String, amount: Double) {

        if (base.equals(currentBase, ignoreCase = true)) {
            amountData.postValue(amount)
        } else {
            callDispose()
            currentBase = base.toUpperCase()
            ratesSubscriber.getRates(base)
                .doOnSubscribe {
                    if (!isLoaded) {
                        dialogState.postValue(1)
                    }
                    disposable = it;
                }
                .delay(BuildConfig.REFRESH_FREQUENCY.toLong(), TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatUntil{ !base.equals(currentBase, ignoreCase = true) }
                .subscribe({ it ->
                    val rates = ArrayList<Rate>()
                    rates.add(Rate(it.base, 1.0))
                    rates.addAll(it.rates.map { Rate(it.key, (it.value)) })

                    currencyList.postValue(rates)
                    if (!isLoaded) {
                        dialogState.postValue(0)
                    }
                    isLoaded = true
                }, {
                    Log.e("Error", "${it.localizedMessage}")
                    errorData.postValue("${it.localizedMessage}")
                })
        }
    }

    private fun callDispose() {
        if (disposable != null && !disposable?.isDisposed!!)
            disposable?.dispose()
    }


    override fun onCleared() {
        super.onCleared()
        callDispose()
    }


}
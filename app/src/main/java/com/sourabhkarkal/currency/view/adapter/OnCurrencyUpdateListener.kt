package com.sourabhkarkal.currency.view.adapter

interface OnCurrencyUpdateListener {

    fun onCurrencyUpdated(symbol: String, amount: Double)
}
package com.sourabhkarkal.currency.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sourabhkarkal.currency.model.Rate
import com.sourabhkarkal.currency.view.adapter.CurrencyConverterAdapter
import com.sourabhkarkal.currency.view.adapter.OnCurrencyUpdateListener
import com.sourabhkarkal.currency.R
import com.sourabhkarkal.currency.viewmodel.ConverterViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_converter.*

class ConverterFragment : Fragment() {

    private var converterViewModel: ConverterViewModel? = null
    private lateinit var adapter: CurrencyConverterAdapter
    private lateinit var rootView: View;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_converter, container, false)
        return rootView;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        converterViewModel = ViewModelProviders.of(this).get(ConverterViewModel::class.java)
        converterViewModel?.checkRates(ConverterViewModel.DEFAULT_SYMBOL, 1.0)
        adapter = CurrencyConverterAdapter(object : OnCurrencyUpdateListener {

            override fun onCurrencyUpdated(symbol: String, amount: Double) {
                converterViewModel?.checkRates(symbol, amount)
            }

        })

        converterViewModel?.getDialogState()?.observe(this, observerDialogStateData)
        converterViewModel?.getAmountLiveData()?.observe(this, observerAmountData)
        converterViewModel?.getCurrencyLiveData()?.observe(this, observerCurrencyLiveData)
        converterViewModel?.getErrorData()?.observe(this, observerErrorData)

        initView()
    }

    private val observerDialogStateData = Observer<Int> {
        if (it == 0)
            showLoading(false)
        else
            showLoading(true)
    }

    private val observerAmountData = Observer<Double> { rate ->
        if (rate != null) {
            updateAmount(rate)
        }
    }

    private val observerCurrencyLiveData = Observer<ArrayList<Rate>> { rates ->
        if (rates != null) {
            updateRatesList(rates)
        }
    }

    private val observerErrorData = Observer<String> {
        Snackbar.make(rootView, getString(R.string.error_msg), 2500).show()
    }


    private fun initView() {
        recyclerCurrencies.setHasFixedSize(true)
        recyclerCurrencies.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
        recyclerCurrencies.adapter = adapter
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

    }

    private fun updateRatesList(rates: ArrayList<Rate>) {
        activity?.runOnUiThread {
            adapter.updateRates(rates)
        }

    }

    private fun updateAmount(amount: Double) {
        activity?.runOnUiThread {
            adapter.updateAmount(amount)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        activity?.runOnUiThread {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

            recyclerCurrencies.visibility = if (isLoading) View.GONE else View.VISIBLE
            recyclerCurrencies.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (isLoading) 0 else 1).toFloat()
            ).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    recyclerCurrencies.visibility = if (isLoading) View.GONE else View.VISIBLE
                }
            })

            progressView.visibility = if (isLoading) View.VISIBLE else View.GONE
            progressView.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (isLoading) 1 else 0).toFloat()
            ).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    progressView.visibility = if (isLoading) View.VISIBLE else View.GONE
                }
            })
        }
    }
}
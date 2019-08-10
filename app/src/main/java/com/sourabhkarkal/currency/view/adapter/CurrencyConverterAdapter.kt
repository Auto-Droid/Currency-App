package com.sourabhkarkal.currency.view.adapter

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.sourabhkarkal.currency.R
import com.sourabhkarkal.currency.model.Rate
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_currency_convert.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.HashMap
import java.util.concurrent.TimeUnit

class CurrencyConverterAdapter(private val onCurrencyUpdateListener: OnCurrencyUpdateListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val symbolPosition = ArrayList<String>()
    private val symbolRate = HashMap<String, Rate>()
    private val publishSubject = PublishSubject.create<Rate>()
    private val decimalFormat = DecimalFormat("#.####")
    private var amount: Double = 1.0

    /**
     * Update the rate of each currency
     */
    fun updateRates(rates: ArrayList<Rate>) {
        if (symbolPosition.isEmpty()) {
            symbolPosition.addAll(rates.map { it.symbol })
        }

        for (rate in rates) {
            symbolRate[rate.symbol] = rate
        }

        notifyItemRangeChanged(0, symbolPosition.size - 1, amount)
    }

    /**
     * Update the amount
     */
    fun updateAmount(amount: Double) {
        this.amount = amount

        notifyItemRangeChanged(0, symbolPosition.size - 1, amount)
    }

    /**
     * Returns the rate at the given position
     */
    private fun rateAtPosition(pos: Int): Rate {
        return symbolRate[symbolPosition[pos]]!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        decimalFormat.roundingMode = RoundingMode.CEILING
        publishSubject.debounce(300, TimeUnit.MILLISECONDS)
            .subscribe(object : io.reactivex.Observer<Rate> {
                override fun onSubscribe(d: Disposable) {
                    //empty
                }

                override fun onNext(rate: Rate) {
                    onCurrencyUpdateListener.onCurrencyUpdated(rate.symbol, rate.rate)
                }

                override fun onError(e: Throwable) {}

                override fun onComplete() {
                    //empty
                }
            })

        return RateViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_currency_convert, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return symbolPosition.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (!payloads.isEmpty()) {
            (holder as RateViewHolder).bind(rateAtPosition(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RateViewHolder).bind(rateAtPosition(position))
    }

    /**
     * Viewholder for the currency
     */
    inner class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {

        var iVCurrencyFlag: ImageView = itemView.ivCurrencyFlag
        var tvCurrencySymbol: TextView = itemView.tvCurrencySymbol
        var tvCurrencyName: TextView = itemView.tvCurrencyName
        var edtCurrencyAmount: EditText = itemView.edtCurrencyAmount
        var rlParentLayout: RelativeLayout = itemView.rlParentLayout
        var symbol: String = ""


        fun bind(rate: Rate) {

            if (symbol != rate.symbol) {
                initView(rate)
                this.symbol = rate.symbol
            }

            if (!edtCurrencyAmount.isFocused) {
                edtCurrencyAmount.setText((decimalFormat.format(amount * rate.rate)))
            }
        }


        private fun initView(rate: Rate) {
            val symbol = rate.symbol.toLowerCase()
            val rateValue = rate.rate

            tvCurrencySymbol.text = rate.symbol

            rlParentLayout.setOnClickListener {

                layoutPosition.takeIf { it > 0 }?.also { currentPosition ->

                    symbolPosition.removeAt(currentPosition).also {
                        symbolPosition.add(0, it)
                    }
                    onCurrencyUpdateListener.onCurrencyUpdated(symbol, rateValue)
                    edtCurrencyAmount.requestFocus()

                    notifyItemMoved(currentPosition, 0)
                }

            }

            edtCurrencyAmount.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edtCurrencyAmount.isFocused) {
                        publishSubject.onNext(Rate(symbol, if(s.toString().isNotEmpty()) s.toString().toDouble() else 0.0 ))
                    }
                }

            })
        }
    }

}
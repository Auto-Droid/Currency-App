package com.sourabhkarkal.currency.view.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.sourabhkarkal.currency.view.fragment.ConverterFragment
import com.sourabhkarkal.currency.view.fragment.AllRatesFragment
import com.sourabhkarkal.currency.R

/**
 *
 * FragmentPagerAdapter implementation to display the main fragments
 */
class ConverterTabFragmentAdapter(val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        const val TabRates = 0
        const val TabConverter = 1
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            TabRates -> AllRatesFragment()
            TabConverter -> ConverterFragment()
            else -> error(
                "there is no type that matches the position $position + make sure your using adapter correctly")
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            TabRates -> context.getString(R.string.title_rates)
            TabConverter -> context.getString(R.string.title_converter)
            else -> ""
        }
    }

}
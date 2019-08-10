package com.sourabhkarkal.currency.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sourabhkarkal.currency.R
import com.sourabhkarkal.currency.view.adapter.ConverterTabFragmentAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var tabFragmentAdapter: ConverterTabFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabFragmentAdapter = ConverterTabFragmentAdapter(this, supportFragmentManager)

        initView()
    }

    /**
     * Setup the view
     */
    private fun initView() {
        mainViewPager.adapter = tabFragmentAdapter
        tabLayout.setupWithViewPager(mainViewPager)
        mainViewPager.currentItem = 1
    }
}

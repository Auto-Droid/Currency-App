package com.sourabhkarkal.currency.interactors.data.remote

import com.sourabhkarkal.currency.BuildConfig
import com.sourabhkarkal.currency.model.RateDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RateService {

    companion object {
        const val BASE_URL = BuildConfig.REVOLUT_BASE_URL;
    }

    @GET("/latest?")
    fun getAllRates(@Query("base") base: String): Single<RateDTO>
}
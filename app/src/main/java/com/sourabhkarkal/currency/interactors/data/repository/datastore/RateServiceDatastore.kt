package com.sourabhkarkal.currency.interactors.data.repository.datastore

import com.sourabhkarkal.currency.model.RateDTO
import com.sourabhkarkal.currency.interactors.data.remote.RateService
import io.reactivex.Single
import javax.inject.Inject


class RateServiceDatastore @Inject constructor(private val rateService: RateService) : RateDatastore {

    override fun getRates(base: String): Single<RateDTO> {
        return rateService.getAllRates(base)
    }
}
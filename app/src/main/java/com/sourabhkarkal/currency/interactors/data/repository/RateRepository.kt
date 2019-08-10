package com.sourabhkarkal.currency.interactors.data.repository

import com.sourabhkarkal.currency.interactors.data.repository.datastore.RateServiceDatastore
import com.sourabhkarkal.currency.model.RateDTO
import io.reactivex.Single
import javax.inject.Inject

class RateRepository @Inject constructor(private val rateRestDatastore: RateServiceDatastore) {

    fun getRates(base: String): Single<RateDTO> {
        return rateRestDatastore.getRates(base)
    }
}
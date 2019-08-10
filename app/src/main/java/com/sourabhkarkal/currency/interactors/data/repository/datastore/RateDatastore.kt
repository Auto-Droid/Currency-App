package com.sourabhkarkal.currency.interactors.data.repository.datastore

import com.sourabhkarkal.currency.model.RateDTO
import io.reactivex.Single

/**
 *
 * Datastore interface to access rates
 */
interface RateDatastore {

    /**
     * Returns the rates using a base symbol
     */
    fun getRates(base: String): Single<RateDTO>

}
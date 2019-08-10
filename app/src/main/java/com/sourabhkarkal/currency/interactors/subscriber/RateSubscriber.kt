package com.sourabhkarkal.currency.interactors.subscriber

import com.sourabhkarkal.currency.model.RateDTO
import com.sourabhkarkal.currency.interactors.data.repository.RateRepository
import io.reactivex.Single
import javax.inject.Inject


class RateSubscriber @Inject constructor(private val rateRepository: RateRepository) {

    fun getRates(base: String): Single<RateDTO> {
        return rateRepository.getRates(base)
    }
}
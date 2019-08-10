package com.sourabhkarkal.currency.di

import com.sourabhkarkal.currency.interactors.data.remote.RateService
import com.sourabhkarkal.currency.interactors.data.repository.RateRepository
import com.sourabhkarkal.currency.interactors.data.repository.datastore.RateServiceDatastore
import com.sourabhkarkal.currency.interactors.subscriber.RateSubscriber
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger2 Module to provides Repository dependencies
 */
@Module
class RepositoryModule {

    @Singleton
    @Provides
    internal fun providesRateRestDatastore(rateService: RateService): RateServiceDatastore {
        return RateServiceDatastore(rateService)
    }

    @Singleton
    @Provides
    internal fun providesRateRepository(rateRestDatastore: RateServiceDatastore): RateRepository {
        return RateRepository(rateRestDatastore)
    }

    @Singleton
    @Provides
    internal fun providesRateUsecase(rateRepository: RateRepository): RateSubscriber {
        return RateSubscriber(rateRepository)
    }
}

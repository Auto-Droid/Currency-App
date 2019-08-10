package com.sourabhkarkal.currency.model

data class RateDTO(val base: String, val date: String, val rates: Map<String, Double>)
package com.nipsr.management.payment.providers.lnbits

import io.smallrye.config.ConfigMapping

const val LNBITS_CONFIG = "nipsr.management.payment.lnbits"

@ConfigMapping(prefix = LNBITS_CONFIG)
interface LNBItsConfig {
    fun enabled(): Boolean
    fun apiKey(): String
}
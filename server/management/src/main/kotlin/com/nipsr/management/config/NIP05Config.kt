package com.nipsr.management.config

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "nipsr.management.nip05")
interface NIP05Config {
    fun minDigits(): Int
    fun basePrice(): Long
}
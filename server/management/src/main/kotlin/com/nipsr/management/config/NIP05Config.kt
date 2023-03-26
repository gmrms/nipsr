package com.nipsr.management.config

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "nipsr.management.nip05")
interface NIP05Config {
    fun allowedByPubkey(): Int
    fun minDigits(): Int
    fun maxDigits(): Int
    fun basePrice(): Long
    fun domains(): List<String>
}
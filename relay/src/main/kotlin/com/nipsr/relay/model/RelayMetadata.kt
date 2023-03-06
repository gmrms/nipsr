package com.nipsr.relay.model

import com.nipsr.payload.nips.NIP_11

@NIP_11
data class RelayMetadata(
    val name: String,
    val description: String,
    val pubkey: String,
    val contact: String,
    val supported_nips: List<Int>,
    val software: String,
    val version: String
)
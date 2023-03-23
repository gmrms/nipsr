package com.nipsr.management.model

data class IdentifierRequest(
    val pubkey: String,
    val identifier: String,
    val domain: String
)
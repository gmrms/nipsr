package com.nipsr.management.model

data class IdentifierRequest(
    var pubkey: String,
    var identifier: String,
    var domain: String
) {
    init {
        identifier = identifier.lowercase()
        domain = domain.lowercase()
        pubkey = pubkey.lowercase()
    }
}
package com.nipsr.relay.model

data class SessionAuth(
    var pubkey: String? = null,
    var authenticated: Boolean = false,
)

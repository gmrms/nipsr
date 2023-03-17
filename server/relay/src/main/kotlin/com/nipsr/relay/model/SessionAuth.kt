package com.nipsr.relay.model

data class SessionAuth(
    var authenticated: Boolean = false,
    var pubkey: String? = null
)

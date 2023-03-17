package com.nipsr.relay.exeptions

class RelayAccessDeniedException(pubkey: String) : TerminateConnectionException(
    "Access not allowed for pubkey: $pubkey"
)
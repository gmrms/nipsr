package com.nipsr.relay.message

import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.nips.NIP_15

@NIP_01
enum class MessageType {
    EVENT,
    REQ,
    CLOSE,
    NOTICE,
    @NIP_15
    EOSE
}
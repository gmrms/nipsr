package com.nipsr.relay.message

import com.nipsr.payload.nips.NIP_01

@NIP_01
enum class MessageType(val client: Boolean, val server: Boolean) {
    EVENT(true, true),
    REQ(true, false),
    CLOSE(true, false),
    NOTICE(false, true)
}
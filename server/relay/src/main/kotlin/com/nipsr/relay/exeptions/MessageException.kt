package com.nipsr.relay.exeptions

import com.nipsr.relay.message.Message
import com.nipsr.relay.message.MessageType

open class MessageException(message: String) : RelayException(message) {
    fun asNotice() = Message(MessageType.NOTICE, "restricted: $message")
}
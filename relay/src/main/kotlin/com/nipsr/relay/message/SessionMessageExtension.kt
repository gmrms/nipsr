package com.nipsr.relay.message

import com.nipsr.payload.ObjectMapperUtils.toJsonString
import javax.websocket.Session

object SessionMessageExtension {

    fun Session.send(message: Message) = this.asyncRemote.sendObject(message.toJsonString()) { result ->
        if(!result.isOK){
            this.asyncRemote.sendText(
                result.exception.asNotice().toJsonString()
            )
        }
    }

    fun Throwable.asNotice() = Message(MessageType.NOTICE, this.message ?: "Unknown error")

}
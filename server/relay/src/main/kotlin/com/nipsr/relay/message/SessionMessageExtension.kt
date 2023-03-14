package com.nipsr.relay.message

import com.nipsr.payload.ObjectMapperUtils.toJsonString
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_20
import com.nipsr.relay.exeptions.EventErrorException
import javax.websocket.Session

object SessionMessageExtension {

    fun Session.send(message: Message) = this.asyncRemote.sendObject(message.toJsonString()) { result ->
        if(!result.isOK){
            this.asyncRemote.sendText(
                result.exception.asNotice().toJsonString()
            )
        }
    }

    @NIP_20
    fun Session.sendResult(event: Event<*>) = this.sendResult(event.id)

    @NIP_20
    fun Session.sendResult(eventId: String) = this.send(Message(
        MessageType.OK,
        eventId,
        true,
        ""
    ))

    @NIP_20
    fun Session.sendResult(relayException: EventErrorException) = this.send(Message(
        MessageType.OK,
        relayException.eventId,
        false,
        relayException.message!!
    ))

    fun Throwable.asNotice() = Message(MessageType.NOTICE, this.message ?: "Unknown error")

}
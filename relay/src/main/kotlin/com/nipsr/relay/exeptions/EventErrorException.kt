package com.nipsr.relay.exeptions

import com.nipsr.payload.model.events.Event

class EventErrorException(
    val eventId: String,
    message: String
) : RelayException(message) {
    constructor(event: Event<*>, message: String?) : this(event.id, message ?: "")
}
package com.nipsr.processor.handlers.spec

import com.nipsr.payload.model.events.Event

abstract class UniquePerKindEventHandler<T : Event<*>> : EventHandler<T>() {
    override suspend fun handleEvent(event: T) {
        eventService.deleteAllOfKindAndPubkey(event.kind, event.pubkey)
    }
}
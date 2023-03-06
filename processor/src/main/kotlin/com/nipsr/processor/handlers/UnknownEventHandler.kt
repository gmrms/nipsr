package com.nipsr.processor.handlers

import com.nipsr.payload.model.events.UnknownEvent
import com.nipsr.payload.nips.NIP_01
import com.nipsr.processor.handlers.spec.EventHandler
import javax.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory

@NIP_01
@ApplicationScoped
class UnknownEventHandler : EventHandler<UnknownEvent>() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun handleEvent(event: UnknownEvent) {
        logger.warn("Received unknown event: kind ${event.kind} | id ${event.id}")
    }

    override fun handlesType() = UnknownEvent::class

}
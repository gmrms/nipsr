package com.nipsr.processor.handlers

import com.nipsr.payload.model.events.UnknownEvent
import javax.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory

@ApplicationScoped
class UnknownEventHandler : EventHandler<UnknownEvent>() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun handleEvent(event: UnknownEvent) {
        logger.warn("Received unknown event: kind ${event.kind} | id ${event.id}")
    }

    override fun handlesType() = UnknownEvent::class

}
package com.nipsr.processor.handlers.spec

import com.nipsr.payload.model.events.Event
import com.nipsr.payload.model.KnownKinds
import com.nipsr.processor.service.ProcessorEventService
import javax.inject.Inject
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

abstract class EventHandler<T : Event<*>> {

    @Inject
    lateinit var eventService: ProcessorEventService

    private val logger = LoggerFactory.getLogger(this::class.java)

    abstract suspend fun handleEvent(event: T)
    abstract fun handlesType(): KClass<T>

    suspend fun handle(event: T) {
        logger.trace("Received event of type '${KnownKinds.fromCode(event.kind).description}' with id '${event.id}'")
        try {
            logger.debug("Handling event '${event.id}'")
            handleEvent(event)
            logger.debug("Event handled '${event.id}'")
            persist(event)
        } catch (e: Exception) {
            logger.error("Error on processing event of type '${KnownKinds.fromCode(event.kind).description}' with id '${event.id}'", e)
        }
    }

    open suspend fun persist(event: T) {
        logger.debug("Persisting event '${event.id}'")
        eventService.persist(event)
        logger.debug("Persisted event '${event.id}'")
    }

}
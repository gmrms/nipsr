package com.nipsr.processor.handlers

import com.nipsr.payload.events.Event
import com.nipsr.processor.service.ProcessorEventService
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

abstract class EventHandler<T : Event<*>> {

    @Inject
    lateinit var eventService: ProcessorEventService

    abstract suspend fun handleEvent(event: T)
    abstract fun handlesType(): KClass<T>

    fun handle(event: T) = runBlocking(Dispatchers.IO) {
        try {
            handleEvent(event)
            persist(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun persist(event: T) {
        eventService.persist(event)
    }

}
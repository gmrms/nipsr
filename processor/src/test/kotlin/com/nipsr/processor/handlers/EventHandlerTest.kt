package com.nipsr.processor.handlers

import com.nipsr.payload.model.EventType
import com.nipsr.payload.model.events.Event
import com.nipsr.processor.handlers.spec.EventHandler
import com.nipsr.processor.service.ProcessorEventService
import io.mockk.Called
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventHandlerTest {

    val processorEventService = mockk<ProcessorEventService>(relaxed = true)

    val eventHandler = spyk<EventHandler<Event<*>>> {
        this.eventService = processorEventService
    }

    val event = mockk<Event<*>>(relaxed = true)

    @Test
    fun `should persist regular events`() = runTest {
        // given
        every { event.readEventType() } returns EventType.REGULAR
        // when
        eventHandler.handle(event)
        // then
        coVerify { processorEventService.persist(any()) }
    }

    @Test
    fun `should replace older events of replaceable events`() = runTest {
        // given
        every { event.readEventType() } returns EventType.REPLACEABLE
        // when
        eventHandler.handle(event)
        // then
        coVerifyOrder {
            processorEventService.deleteOldersOfKindAndPubkey(any(), any(), any())
            processorEventService.persist(any())
        }
    }

    @Test
    fun `should do nothing on ephemeral events`() = runTest {
        // given
        every { event.readEventType() } returns EventType.EPHEMERAL
        // when
        eventHandler.handle(event)
        // then
        coVerify { processorEventService wasNot Called }
    }

}
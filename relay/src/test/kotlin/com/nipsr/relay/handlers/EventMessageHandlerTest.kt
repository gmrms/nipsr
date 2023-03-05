package com.nipsr.relay.handlers

import com.nipsr.payload.ObjectMapperUtils.mapTo
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.TestContexts.defaultSessionsContext
import com.nipsr.relay.TestEvents
import com.nipsr.relay.filters.EventFilter
import com.nipsr.relay.handlers.spec.MessageParts
import io.mockk.coVerify
import io.mockk.mockk
import javax.enterprise.inject.Instance
import kotlinx.coroutines.test.runTest
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.junit.Test

class EventMessageHandlerTest {

    val eventEmmiter = mockk<Emitter<Event<*>>>(relaxed = true)
    val eventFilters = mockk<Instance<EventFilter>>(relaxed = true)

    val reqMessageHandler = EventMessageHandler(eventEmmiter, eventFilters)

    @Test
    @NIP_01
    fun `should handle EVENT message`() = runTest {
        reqMessageHandler.handleMessage(defaultSessionsContext, MessageParts(
            "EVENT",
            TestEvents.validEvents.first().mapTo(HashMap::class.java)
        ))
        coVerify(atLeast = 1) { eventEmmiter.send(any<Event<*>>()) }
    }

}
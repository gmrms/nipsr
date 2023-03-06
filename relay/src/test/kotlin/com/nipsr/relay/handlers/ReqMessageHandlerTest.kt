package com.nipsr.relay.handlers

import com.nipsr.payload.ObjectMapperUtils.mapTo
import com.nipsr.payload.model.Filter
import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.TestContexts.defaultSessionsContext
import com.nipsr.relay.TestEvents
import com.nipsr.relay.handlers.spec.MessageParts
import com.nipsr.relay.service.RelayEventService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test

@NIP_01
class ReqMessageHandlerTest {

    val eventService = mockk<RelayEventService> {
        coEvery { findByFilters(any()) } returns TestEvents.validEvents.asFlow()
    }

    val reqMessageHandler = ReqMessageHandler(eventService)

    @Test
    fun `should handle REQ message`() = runTest {
        reqMessageHandler.handleMessage(defaultSessionsContext, MessageParts(
            "REQ",
            UUID.randomUUID().toString(),
            Filter(
                kinds = listOf(1, 2),
            ).mapTo(HashMap::class.java)
        ))
        coVerify(atLeast = 1) { eventService.findByFilters(any()) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should deny too large subscription Ids`() = runTest {
        val tooLarge = "a".repeat(1000)
        reqMessageHandler.handleMessage(defaultSessionsContext, MessageParts(
            "REQ",
            tooLarge,
            Filter(
                kinds = listOf(1, 2),
            ).mapTo(HashMap::class.java)
        ))
    }

}
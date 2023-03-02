package com.nipsr.relay.handlers

import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.TestContexts.defaultSessionsContext
import com.nipsr.relay.handlers.spec.MessageParts
import java.util.*
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CloseMessageHandlerTest {

    val reqMessageHandler = CloseMessageHandler()

    @Test
    @NIP_01
    fun `should handle CLOSE message`() = runTest {
        reqMessageHandler.handleMessage(defaultSessionsContext, MessageParts(
            "CLOSE",
            UUID.randomUUID().toString()
        ))
    }

    @Test(expected = IllegalArgumentException::class)
    @NIP_01
    fun `should deny too large subscription Ids`() = runTest {
        val tooLarge = "a".repeat(1000)
        reqMessageHandler.handleMessage(defaultSessionsContext, MessageParts(
            "CLOSE",
            tooLarge
        ))
    }

}
package com.nipsr.relay.handlers.spec

import com.nipsr.relay.message.MessageType
import com.nipsr.relay.model.SessionsContext

/**
 * A message handler is responsible for handling a specific message type.
 */
interface MessageHandler {

    /**
     * Handles a message of the type [handlesType]
     */
    suspend fun handleMessage(sessionsContext: SessionsContext, messageParts: MessageParts)

    /**
     * The type of message this handler handles
     */
    fun handlesType(): MessageType

    /**
     * A helper method to perform the conversion before calling [handleMessage]
     */
    suspend fun handle(sessionsContext: SessionsContext, parts: List<Any>) {
        handleMessage(sessionsContext, MessageParts.fromList(parts))
    }

    /**
     * Helper to define the message structure
     */
    fun messageSpec(): MessageSpec

    fun spec(key: String) = messageSpec()[key]!!

}
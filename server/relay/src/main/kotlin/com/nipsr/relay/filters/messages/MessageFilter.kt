package com.nipsr.relay.filters.messages

import com.nipsr.relay.filters.FilterType
import com.nipsr.relay.message.Message
import com.nipsr.relay.message.MessageType
import com.nipsr.relay.model.SessionsContext

/**
 * A message filter is used to filter messages sent by clients.
 */
interface MessageFilter : Comparable<MessageFilter> {

    /**
     * Returns true if the message should be allowed should be processed by the server.
     * @throws
     */
    suspend fun filter(message: Message, sessionContext: SessionsContext)

    /**
     * Returns the type of the filter.
     */
    fun type(): FilterType

    fun handlesType(messageType: MessageType): Boolean

    fun order(): Int = 0

    override fun compareTo(other: MessageFilter): Int {
        return order().compareTo(other.order())
    }

}
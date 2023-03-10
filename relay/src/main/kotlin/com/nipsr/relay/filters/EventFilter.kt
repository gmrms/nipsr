package com.nipsr.relay.filters

import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_20

/**
 * An event filter is used to filter events that are sent by and to a client.
 */
interface EventFilter {

    /**
     * Returns true if the event should be allowed to be sent to the client.
     */
    fun filter(event: Event<*>): Pair<Boolean, String?>

    /**
     * Returns the type of the filter.
     */
    fun type(): FilterType

    @NIP_20
    companion object {

        infix fun Boolean.invalid(message: String) = this to "invalid: $message"
        infix fun Boolean.blocked(message: String) = this to "blocked: $message"
        infix fun Boolean.pow(message: String) = this to "pow: $message"
        infix fun Boolean.rateLimited(message: String) = this to "rate-limited: $message"
        infix fun Boolean.error(message: String) = this to "error: $message"
        fun ok() = true to ""

    }

}
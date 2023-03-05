package com.nipsr.relay.filters

import com.nipsr.payload.model.events.Event

/**
 * An event filter is used to filter events that are sent by and to a client.
 */
interface EventFilter {

    /**
     * Returns true if the event should be allowed to be sent to the client.
     */
    fun filter(event: Event<*>): Boolean

    /**
     * Returns the type of the filter.
     */
    fun type(): FilterType

}
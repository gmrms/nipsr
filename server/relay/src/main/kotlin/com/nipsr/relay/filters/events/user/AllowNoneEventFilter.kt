package com.nipsr.relay.filters.events.user

import com.nipsr.payload.model.events.Event
import com.nipsr.relay.filters.events.EventFilter
import com.nipsr.relay.filters.events.EventFilter.Companion.blocked
import com.nipsr.relay.filters.FilterType

/**
 * An event filter that allows no events.
 */
class AllowNoneEventFilter : EventFilter {
    override suspend fun filter(event: Event<*>) = false blocked "Not allowed"
    override fun type() = FilterType.USER
}
package com.nipsr.relay.filters.user

import com.nipsr.payload.model.events.Event
import com.nipsr.relay.filters.EventFilter
import com.nipsr.relay.filters.FilterType

/**
 * An event filter that allows no events.
 */
class AllowNoneEventFilter : EventFilter {
    override fun filter(event: Event<*>) = false
    override fun type() = FilterType.USER
}
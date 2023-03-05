package com.nipsr.relay.filters.user

import com.nipsr.payload.model.events.Event
import com.nipsr.payload.model.Filter
import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.filters.EventFilter
import com.nipsr.relay.filters.FilterType

/**
 * An event filter that allows events that match the filters specified by a client.
 */
@NIP_01
class UserEventFilter(
    val filters: Iterable<Filter>
) : EventFilter {

    constructor(filter: Filter) : this(listOf(filter))

    override fun filter(event: Event<*>): Boolean {
        for(filter in filters) {
            if(applyFilter(filter, event)) return true
        }
        return false
    }

    private fun applyFilter(filter: Filter, event: Event<*>): Boolean {
        if(!filter.ids.any { event.id.startsWith(it) }) return false
        if(!filter.authors.any { event.pubkey.startsWith(it) }) return false
        if(filter.kinds?.contains(event.kind) == false) return false
        //if(!filter.tags.any { tag -> event.tags.any { it.count { tag.contains(it) } >= 2 } }) return false
        if(filter.since != null && event.created_at < filter.since!!) return false
        if(filter.until != null && event.created_at > filter.until!!) return false
        return true
    }

    private fun <T> List<T>?.any(predicate: (T) -> Boolean): Boolean {
        if (this == null) return true
        for (element in this) {
            if (predicate(element)) {
                return true
            }
        }
        return false
    }

    override fun type() = FilterType.USER

}

//q: what does the command mvn -B do?
//a:
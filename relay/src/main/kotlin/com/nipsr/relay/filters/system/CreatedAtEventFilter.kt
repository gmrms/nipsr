package com.nipsr.relay.filters.system

import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_22
import com.nipsr.relay.config.NipsrRelaySettings
import com.nipsr.relay.filters.EventFilter
import com.nipsr.relay.filters.EventFilter.Companion.invalid
import com.nipsr.relay.filters.EventFilter.Companion.ok
import com.nipsr.relay.filters.FilterType
import javax.enterprise.context.ApplicationScoped

/**
 * An event filter that validates the signature of an event.
 */
@NIP_22
@ApplicationScoped
class CreatedAtEventFilter(
    private val settings: NipsrRelaySettings
) : EventFilter {

    /**
     * Returns true if the event was created within the last [NipsrRelaySettings.maxCreatedAtDriftMinutes] minutes.
     */
    override fun filter(event: Event<*>) : Pair<Boolean, String?> {
        val maxCreatedAtDriftMinutes = settings.maxCreatedAtDriftMinutes()
        val createdAt = event.created_at
        val now = System.currentTimeMillis()
        val createdAtDrift = now - createdAt
        val createdAtDriftMinutes = createdAtDrift / 1000 / 60
        return if(createdAtDriftMinutes <= maxCreatedAtDriftMinutes){
            ok()
        } else {
            false invalid  "Event created_at is too far in the past, " +
                           "the allowed drift is $maxCreatedAtDriftMinutes minutes"
        }
    }

    override fun type() = FilterType.GLOBAL

}
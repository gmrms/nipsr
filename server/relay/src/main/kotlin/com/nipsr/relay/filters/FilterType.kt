package com.nipsr.relay.filters

/**
 * Type of the filter of an [EventFilter].
 *
 * GLOBAL -> Filters that are applied to all events.
 * USER -> Filters that are applied to events that are sent to a specific client.
 */
enum class FilterType {
    GLOBAL,
    USER
}
package com.nipsr.payload.model

import com.nipsr.payload.Constants

enum class EventType(val kinds: IntRange, vararg val additions: KnownKinds) {
    REGULAR(Constants.REGULAR_EVENTS_RANGE, KnownKinds.TEXT_NOTE, KnownKinds.RECOMMEND_SERVER, KnownKinds.DELETION),
    REPLACEABLE(Constants.REPLACEABLE_EVENTS_RANGE, KnownKinds.CONTACT_LIST, KnownKinds.SET_METADATA),
    EPHEMERAL(Constants.EPHEMERAL_EVENTS_RANGE);
    companion object {
        fun fromKind(kind: Int) = values().first {
            kind in it.kinds ||
            kind in it.additions.map { known ->
                known.kind
            }
        }
    }
}
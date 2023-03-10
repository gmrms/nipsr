package com.nipsr.payload.utils

import com.nipsr.payload.ObjectMapperUtils
import com.nipsr.payload.model.EventType
import com.nipsr.payload.model.KnownKinds
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_16

object EventsExtensions {

    @NIP_16
    fun Event<*>.getEventType() = EventType.fromKind(kind)

    fun <T> Event<T>.readContent() : T = ObjectMapperUtils.objectMapper
        .readValue(content, KnownKinds.fromCode(kind).contentType.java) as T

}
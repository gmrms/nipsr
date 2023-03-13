package com.nipsr.payload.factory

import com.nipsr.payload.model.KnownKinds
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.model.inputs.EventInput
import kotlin.reflect.full.createInstance

object EventFactory {

    fun createEvent(eventInput: EventInput): Event<*> {
        val kind = KnownKinds.fromCode(eventInput.kind)
        val event = kind.type.createInstance() as Event<*>
        return event.fromDTO(eventInput)
    }

}

fun EventInput.toEvent() : Event<*> = EventFactory.createEvent(this)
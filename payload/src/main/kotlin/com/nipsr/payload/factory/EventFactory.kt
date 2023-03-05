package com.nipsr.payload.factory

import com.nipsr.payload.model.KnownKinds
import com.nipsr.payload.model.inputs.EventInput
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.model.events.RecommendServerEvent
import com.nipsr.payload.model.events.SetMetadataEvent
import com.nipsr.payload.model.events.TextNoteEvent
import com.nipsr.payload.model.events.UnknownEvent

class EventFactory {

    companion object {
        val instance = EventFactory()
    }

    fun createEvent(eventInput: EventInput): Event<*> {
        val kind = KnownKinds.fromCode(eventInput.kind)
        return when (kind) {
            KnownKinds.SET_METADATA -> SetMetadataEvent().fromDTO(eventInput)
            KnownKinds.TEXT_NOTE -> TextNoteEvent().fromDTO(eventInput)
            KnownKinds.RECOMMEND_SERVER -> RecommendServerEvent().fromDTO(eventInput)
            KnownKinds.UNKNOWN -> UnknownEvent().fromDTO(eventInput)
        }
    }

}

fun EventInput.toEvent() : Event<*> = EventFactory.instance.createEvent(this)
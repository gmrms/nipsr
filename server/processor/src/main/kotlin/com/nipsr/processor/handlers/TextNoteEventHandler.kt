package com.nipsr.processor.handlers

import com.nipsr.payload.model.events.TextNoteEvent
import com.nipsr.payload.nips.NIP_01
import com.nipsr.processor.handlers.spec.EventHandler
import javax.enterprise.context.ApplicationScoped

@NIP_01
@ApplicationScoped
class TextNoteEventHandler : EventHandler<TextNoteEvent>() {

    override suspend fun handleEvent(event: TextNoteEvent) {
    }

    override fun handlesType() = TextNoteEvent::class

}
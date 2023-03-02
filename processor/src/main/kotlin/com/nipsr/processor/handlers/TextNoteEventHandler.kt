package com.nipsr.processor.handlers

import com.nipsr.payload.events.TextNoteEvent
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TextNoteEventHandler : EventHandler<TextNoteEvent>() {

    override suspend fun handleEvent(event: TextNoteEvent) {
        println("Received text_note event")
    }

    override fun handlesType() = TextNoteEvent::class

}
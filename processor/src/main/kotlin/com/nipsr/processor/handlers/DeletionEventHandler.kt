package com.nipsr.processor.handlers

import com.nipsr.payload.Constants.EVENT_TAG
import com.nipsr.payload.model.events.DeletionEvent
import com.nipsr.payload.nips.NIP_09
import com.nipsr.processor.handlers.spec.EventHandler
import javax.enterprise.context.ApplicationScoped

@NIP_09
@ApplicationScoped
class DeletionEventHandler : EventHandler<DeletionEvent>() {

    override suspend fun handleEvent(event: DeletionEvent) {
        val eventTagValues = event.tags.filter { it.tag == EVENT_TAG }.map { it.value }
        eventService.deleteByIdsAndPubkey(eventTagValues, event.pubkey)
    }
    override fun handlesType() = DeletionEvent::class
}